## Aplicación POKEDEX en Spring Boot ##

Los servicios están expuestos en Heroku:
https://mdy-pokedex.herokuapp.com/

En ella existen dos recursos disponibles que nos permitiran acceder 
a un listado de Pokemones y otro que nos permitira a obtener el detalle.

La aplicación consume la API: https://pokeapi.co/docs/v2

Para acceder al recurso que nos permitirá acceder a un listado de pokemones,
debemos invocar a:

https://mdy-pokedex.herokuapp.com/api/v1/pokemon/?page={page}
Nota: Si no envias el número de página, por defecto será el 1.

Para obtener un detalle:

https://mdy-pokedex.herokuapp.com/api/v1/pokemon/{id}

La aplicación utiliza un servicio de Cache de RedisLabs.

## Estructura ##
La estructura de proyecto tiene a respetar los conceptos de SOLID y 
tambien la infraestructura limpia.
Está dividido en diferentes capas:

* Adapters: Donde se encuentra toda la lógica de integraciones
* Application: Es la capa de negocio
* Config: Es la capa donde se realizan las creaciones de beans que el proyecto puede tener