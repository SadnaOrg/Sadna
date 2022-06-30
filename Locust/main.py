from locust import HttpUser, task, constant_pacing


class myTasks(HttpUser):
    @task(1)
    def dummy(self):
        pass
