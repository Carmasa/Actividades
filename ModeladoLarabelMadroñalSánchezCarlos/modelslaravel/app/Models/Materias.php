<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Materias extends Model
{
    //

    public function alumnos()
    {
        return $this->belongsToMany(Alumnos::class, 'alumnomateria', 'materia_id', 'alumno_id')->withTimestamps();
    }
}
