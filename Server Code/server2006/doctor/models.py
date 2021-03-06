from django.db import models
from django.contrib.auth.models import User
from clinic.models import Clinic

class Doctor(models.Model):
	account = models.ForeignKey(User)
	name = models.CharField(max_length=100)
	type = models.IntegerField()
	clinic = models.ForeignKey(Clinic, related_name='doctors', on_delete=models.SET_NULL, null=True)