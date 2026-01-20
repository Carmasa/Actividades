<?php

namespace Database\Factories;

use App\Models\Materias;
use Illuminate\Database\Eloquent\Factories\Factory;

class MateriasFactory extends Factory
{
    protected $model = Materias::class;

    public function definition(): array
    {
        return [
            'nombre' => $this->faker->word(),
            'descripcion' => $this->faker->sentence(),
            'codigo' => $this->faker->unique()->bothify('???###'),
        ];
    }
}
