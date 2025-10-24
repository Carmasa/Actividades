-- Cambiar al contexto master (muy importante antes de borrar la base)
USE master;
GO

-- Si la base de datos existe, eliminarla forzando el cierre de conexiones
IF DB_ID(N'Restaurante') IS NOT NULL
BEGIN
    ALTER DATABASE Restaurante SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE Restaurante;
END;
GO

-- Crear la base de datos de nuevo
CREATE DATABASE Restaurante;
GO

-- Cambiar al contexto de la nueva base de datos
USE Restaurante;
GO

-- Crear tablas
CREATE TABLE dbo.Carnes (
    Id INT PRIMARY KEY,
    Nombre VARCHAR(50),
    Descripcion VARCHAR(60),
    Precio DECIMAL(8,2),
    Disponibilidad VARCHAR(50)
);

CREATE TABLE dbo.Pescados (
    Id INT PRIMARY KEY,
    Nombre VARCHAR(50),
    Descripcion VARCHAR(60),
    Precio DECIMAL(8,2),
    Disponibilidad VARCHAR(50)
);

CREATE TABLE dbo.Veganos (
    Id INT PRIMARY KEY,
    Nombre VARCHAR(50),
    Descripcion VARCHAR(60),
    Precio DECIMAL(8,2),
    Disponibilidad VARCHAR(50)
);

CREATE TABLE dbo.Postres (
    Id INT PRIMARY KEY,
    Nombre VARCHAR(50),
    Descripcion VARCHAR(60),
    Precio DECIMAL(8,2),
    Disponibilidad VARCHAR(50)
);

CREATE TABLE dbo.Vinos (
    Id INT PRIMARY KEY,
    Nombre VARCHAR(50),
    Descripcion VARCHAR(60),
    Precio DECIMAL(8,2),
    Disponibilidad VARCHAR(50)
);

CREATE TABLE dbo.Especialidades (
    Id INT PRIMARY KEY,
    Nombre VARCHAR(50),
    Descripcion VARCHAR(60),
    Precio DECIMAL(8,2),
    Disponibilidad VARCHAR(50)
);

CREATE TABLE dbo.Pedido (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    Fecha DATETIME DEFAULT GETDATE(),
    IdCarne INT NULL,
    IdPescado INT NULL,
    IdVegano INT NULL,
    IdPostre INT NULL,
    IdVino INT NULL,
    IdEspecialidad INT NULL,
    Total DECIMAL(10,2) NULL,

    CONSTRAINT FK_Pedido_Carne FOREIGN KEY (IdCarne) REFERENCES dbo.Carnes(Id),
    CONSTRAINT FK_Pedido_Pescado FOREIGN KEY (IdPescado) REFERENCES dbo.Pescados(Id),
    CONSTRAINT FK_Pedido_Vegano FOREIGN KEY (IdVegano) REFERENCES dbo.Veganos(Id),
    CONSTRAINT FK_Pedido_Postre FOREIGN KEY (IdPostre) REFERENCES dbo.Postres(Id),
    CONSTRAINT FK_Pedido_Vino FOREIGN KEY (IdVino) REFERENCES dbo.Vinos(Id),
    CONSTRAINT FK_Pedido_Especialidad FOREIGN KEY (IdEspecialidad) REFERENCES dbo.Especialidades(Id)
);

GO

INSERT INTO Carnes (Id, Nombre, Descripcion, Precio, Disponibilidad) VALUES
(1, 'Solomillo de ternera', 'Corte tierno con salsa de vino tinto', 18.50, 'Disponible'),
(2, 'Entrecot a la parrilla', 'Asado al punto con guarnición de patatas', 16.00, 'Disponible'),
(3, 'Pollo al ajillo', 'Jugoso con toque de ajo y perejil', 12.50, 'Disponible'),
(4, 'Costillas BBQ', 'Glaseadas con salsa barbacoa casera', 14.75, 'Disponible'),
(5, 'Carrillada de cerdo', 'Cocida lentamente en vino tinto', 15.20, 'Disponible'),
(6, 'Hamburguesa gourmet', 'Con queso cheddar y cebolla caramelizada', 11.90, 'Disponible'),
(7, 'Chuletón de vaca', 'Corte premium de 500g', 24.00, 'Disponible'),
(8, 'Pechuga rellena', 'Rellena de espinacas y queso', 13.80, 'Disponible');

INSERT INTO Pescados (Id, Nombre, Descripcion, Precio, Disponibilidad) VALUES
(1, 'Salmón a la plancha', 'Con toque de limón y eneldo', 17.50, 'Disponible'),
(2, 'Lubina al horno', 'Con verduras mediterráneas', 18.00, 'Disponible'),
(3, 'Bacalao con tomate', 'En salsa casera de tomate y ajo', 15.80, 'Disponible'),
(4, 'Merluza a la romana', 'Rebozada y frita al momento', 13.50, 'Disponible'),
(5, 'Atún rojo', 'A la plancha con sésamo tostado', 19.90, 'Disponible'),
(6, 'Pulpo a la gallega', 'Con pimentón y aceite de oliva', 20.50, 'Disponible'),
(7, 'Calamar relleno', 'De verduras y arroz', 16.30, 'Disponible'),
(8, 'Dorada a la sal', 'Cocción tradicional con sal marina', 18.70, 'Disponible');

INSERT INTO Veganos (Id, Nombre, Descripcion, Precio, Disponibilidad) VALUES
(1, 'Hamburguesa vegana', 'De lentejas y remolacha', 10.50, 'Disponible'),
(2, 'Ensalada de quinoa', 'Con aguacate y vinagreta de lima', 9.80, 'Disponible'),
(3, 'Tofu salteado', 'Con verduras al wok y salsa de soja', 11.20, 'Disponible'),
(4, 'Curry de garbanzos', 'Con leche de coco y cúrcuma', 12.00, 'Disponible'),
(5, 'Tacos veganos', 'Rellenos de heura y guacamole', 10.90, 'Disponible'),
(6, 'Pasta integral', 'Con pesto de albahaca y nueces', 11.50, 'Disponible'),
(7, 'Berenjena rellena', 'De quinoa y verduras asadas', 12.40, 'Disponible'),
(8, 'Crema de calabaza', 'Con semillas tostadas y aceite de trufa', 8.70, 'Disponible');

INSERT INTO Postres (Id, Nombre, Descripcion, Precio, Disponibilidad) VALUES
(1, 'Tarta de queso', 'Con base de galleta y mermelada de frutos rojos', 5.50, 'Disponible'),
(2, 'Brownie de chocolate', 'Con nueces y bola de helado', 5.90, 'Disponible'),
(3, 'Flan casero', 'Con caramelo y nata montada', 4.80, 'Disponible'),
(4, 'Coulant de chocolate', 'Con interior fundido', 6.00, 'Disponible'),
(5, 'Tiramisú', 'Clásico italiano con cacao', 5.70, 'Disponible'),
(6, 'Helado artesanal', 'Vainilla, chocolate o fresa', 4.50, 'Disponible'),
(7, 'Mousse de limón', 'Refrescante y ligera', 4.90, 'Disponible'),
(8, 'Tarta de manzana', 'Con canela y helado de vainilla', 5.20, 'Disponible');

INSERT INTO Vinos (Id, Nombre, Descripcion, Precio, Disponibilidad) VALUES
(1, 'Rioja Crianza', 'Tinto suave con notas de roble', 15.00, 'Disponible'),
(2, 'Ribera del Duero', 'Tinto intenso y afrutado', 18.50, 'Disponible'),
(3, 'Albariño', 'Blanco gallego fresco y aromático', 14.00, 'Disponible'),
(4, 'Verdejo', 'Blanco seco con matices frutales', 13.00, 'Disponible'),
(5, 'Rosado Navarra', 'Equilibrado y refrescante', 12.00, 'Disponible'),
(6, 'Cava Brut', 'Espumoso ideal para celebraciones', 16.00, 'Disponible'),
(7, 'Vino de Oporto', 'Dulce y aromático', 17.50, 'Disponible'),
(8, 'Malbec argentino', 'Cuerpo medio, notas de ciruela', 19.00, 'Disponible');

INSERT INTO Especialidades (Id, Nombre, Descripcion, Precio, Disponibilidad) VALUES
(1, 'Paella valenciana', 'Arroz con pollo, conejo y verduras', 16.50, 'Disponible'),
(2, 'Fideuà', 'Fideos con marisco y alioli', 15.80, 'Disponible'),
(3, 'Rabo de toro', 'Estofado tradicional cordobés', 17.90, 'Disponible'),
(4, 'Cochinillo asado', 'Crujiente, al estilo segoviano', 22.00, 'Disponible'),
(5, 'Pulpo a la brasa', 'Con puré de patata y pimentón', 20.50, 'Disponible'),
(6, 'Arroz negro', 'Con calamar y alioli', 16.80, 'Disponible'),
(7, 'Callos a la madrileña', 'Guiso tradicional con chorizo y morcilla', 14.70, 'Disponible'),
(8, 'Cordero al horno', 'Con romero y vino blanco', 21.00, 'Disponible');