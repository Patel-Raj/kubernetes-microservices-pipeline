# GCP provider
provider "google" {
  credentials = file("./k8s-project-id-1-71a12bf27342.json")
  project     = "k8s-project-id-1"
  region      = "us-central1"
  zone        = "us-central1-c"
}

# Google Kubernetes Engine
resource "google_container_cluster" "gke-cluster-1" {
  name                     = "gke-cluster-1"
  location                 = "us-central1-c"
  initial_node_count       = 1
  remove_default_node_pool = true
}

# Node pool
resource "google_container_node_pool" "node-pool" {
  name       = "node-pool"
  cluster    = google_container_cluster.gke-cluster-1.id
  node_count = 1

  node_config {
    preemptible  = true
    machine_type = "e2-medium"
    disk_size_gb = 10
    disk_type    = "pd-standard"
    image_type   = "COS_CONTAINERD"
  }
}

