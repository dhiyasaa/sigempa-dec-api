from fastapi import FastAPI
from pydantic import BaseModel

import joblib
import numpy as np
import os

import tensorflow as tf
import tensorflow.keras.backend as K
from tensorflow.keras.layers import Layer
from tensorflow.keras.models import load_model


# ==================================
# Clustering Layer
# ==================================

class ClusteringLayer(Layer):

    def __init__(self, n_clusters, alpha=1.0, **kwargs):
        super().__init__(**kwargs)
        self.n_clusters = n_clusters
        self.alpha = alpha

    def build(self, input_shape):

        input_dim = input_shape[1]

        self.clusters = self.add_weight(
            shape=(self.n_clusters, input_dim),
            initializer="glorot_uniform",
            trainable=True,
            name="clusters"
        )

    def call(self, inputs):

        q = 1.0 / (
            1.0 +
            (
                K.sum(
                    K.square(
                        K.expand_dims(inputs, axis=1) - self.clusters
                    ),
                    axis=2
                ) / self.alpha
            )
        )

        q = q ** ((self.alpha + 1.0) / 2.0)

        q = K.transpose(
            K.transpose(q) / K.sum(q, axis=1)
        )

        return q


BASE_DIR = os.path.dirname(os.path.abspath(__file__))

scaler = joblib.load(
    os.path.join(BASE_DIR, "scaler.pkl")
)

encoder = load_model(
    os.path.join(BASE_DIR, "encoder.keras"),
    compile=False
)

dec_model = load_model(
    os.path.join(BASE_DIR, "dec_model.keras"),
    custom_objects={
        "ClusteringLayer": ClusteringLayer
    },
    compile=False
)

# Ambil centroid dari ClusteringLayer
clustering_layer = dec_model.get_layer(index=-1)
centroids = clustering_layer.get_weights()[0]

mapping = {

    0: {
        "label": "Risiko Rendah",
        "status": "AMAN",
        "color": "#22C55E"
    },

    1: {
        "label": "Risiko Sedang",
        "status": "WASPADA",
        "color": "#FACC15"
    },

    2: {
        "label": "Risiko Tinggi",
        "status": "SIAGA",
        "color": "#EF4444"
    }

}

app = FastAPI()


class Gempa(BaseModel):

    magnitudo: float
    kedalaman: float


@app.get("/")
def home():

    return {
        "message": "SiGempa DEC API Running"
    }


@app.post("/predict")
def predict(data: Gempa):

    # ==========================
    # Input
    # ==========================
    X = np.array([
        [data.magnitudo, data.kedalaman]
    ])

    # ==========================
    # Normalisasi
    # ==========================
    X_scaled = scaler.transform(X)

    # ==========================
    # Encoder
    # ==========================
    latent = encoder.predict(
        X_scaled,
        verbose=0
    )

    # ==========================
    # DEC Prediction
    # ==========================
    q = dec_model.predict(
        X_scaled,
        verbose=0
    )

    cluster = int(np.argmax(q))

    return {

        "input": {
            "magnitudo": float(data.magnitudo),
            "kedalaman": float(data.kedalaman)
        },

        "normalized": {
            "magnitudo": float(X_scaled[0][0]),
            "kedalaman": float(X_scaled[0][1])
        },

        "latent": [
            float(v)
            for v in latent[0]
        ],

        "centroids": [
            [
                float(x)
                for x in row
            ]
            for row in centroids
        ],

        "probability": [
            float(v)
            for v in q[0]
        ],

        "cluster": cluster,

        "label": mapping[cluster]["label"],

        "status": mapping[cluster]["status"],

        "color": mapping[cluster]["color"]

    }