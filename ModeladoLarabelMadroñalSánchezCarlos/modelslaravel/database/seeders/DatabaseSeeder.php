<?php

namespace Database\Seeders;

use App\Models\Materias;
use App\Models\Alumnos;
use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;

class DatabaseSeeder extends Seeder
{
    use WithoutModelEvents;

    /**
     * Seed the application's database.
     */
    public function run(): void
    {
        $alumno1 = Alumnos::create([
            'nombre' => 'Carlos',
            'apellido' => 'Madroñal',
            'email' => 'Carlos.Madroñal@gmail.com',
            'edad' => 20
        ]);
        $alumno2 = Alumnos::create([
            'nombre' => 'Ana',
            'apellido' => 'García',
            'email' => 'ana.garcia@example.com',
            'edad' => 21
        ]);

        $materia1 = Materias::create([
            'nombre' => 'Matemáticas',
            'descripcion' => 'Estudio de números y operaciones',
            'codigo' => 'MAT101'
        ]);
        $materia2 = Materias::create([
            'nombre' => 'Historia',
            'descripcion' => 'Estudio de eventos pasados',
            'codigo' => 'HIS101'
        ]);
        
        $alumno1->materias()->attach([$materia1->id, $materia2->id]);
        $alumno2->materias()->attach([$materia2->id]);
    }
}
