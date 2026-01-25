<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Alumnos extends Model
{
    //


    public function materias()
    {
        return $this->belongsToMany(Materias::class, 'alumnomateria', 'alumno_id', 'materia_id')->withTimestamps();
    }
}
