# -*- coding: utf-8 -*-
# Generated by Django 1.10.3 on 2016-11-21 09:44
from __future__ import unicode_literals

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('RestFramework', '0015_comment'),
    ]

    operations = [
        migrations.AlterField(
            model_name='comment',
            name='belongsTo',
            field=models.ForeignKey(blank=True, default=None, null=True, on_delete=django.db.models.deletion.CASCADE, to='RestFramework.LocationNote'),
        ),
    ]