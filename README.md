# Buy-SellNow

>BuySellNow es una aplicación de compra-venta en línea para productos nuevos y de segunda mano. Con BuySellNow, los usuarios pueden registrarse en la aplicación y > publicar productos para la venta o comprar productos que ya estén en la venta en la aplicación.

# Características
>Registro de usuario: los usuarios pueden registrarse en la aplicación con sus detalles personales y contraseña.
Publicación de productos: los usuarios registrados pueden publicar productos para la venta con información detallada como nombre, categoría, descripción, precio, peso, etc.
>Comprar productos: los usuarios pueden navegar y comprar productos publicados en la aplicación.
Chat de compra-venta: los usuarios pueden chatear con el vendedor para hacer preguntas sobre el producto o acordar detalles de la transacción.
Productos favoritos: los usuarios pueden marcar productos como favoritos y acceder a ellos fácilmente en una lista separada.
Perfil de usuario: los usuarios tienen un perfil en la aplicación que muestra su ubicación, puntuación, historial de compras y ventas, etc.

# Interfaces de datos
# BuySellNow utiliza las siguientes interfaces de datos en Kotlin:
>Clase User: representa a un usuario registrado en la aplicación. Almacena información como el ID de usuario, nombre, apellido, correo electrónico, contraseña, fecha de registro y verificación de cuenta.

>Clase UserDetail: representa los detalles adicionales de un usuario en la aplicación. Almacena información como la ubicación del usuario, su puntuación, historial de compras y ventas, imágenes de perfil, suscripciones, productos favoritos, etc.
>Clase ProductosFavoritos: representa los productos favoritos de un usuario. Almacena información como el ID de producto y el ID de usuario.

>Clase Chat: representa un chat entre un comprador y un vendedor en la aplicación. Almacena información como el ID de chat, ID de usuario del vendedor, ID de usuario del comprador, ID de producto, fecha y hora del chat.

>Clase Productos: representa un producto publicado para la venta en la aplicación. Almacena información como el ID de producto, nombre, categoría, descripción, precio, peso, imágenes, color, marca, título, valoración, informacion_producto, estado, estado_producto, delivery, UserId.
