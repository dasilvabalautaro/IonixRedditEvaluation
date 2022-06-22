# IonixRedditEvaluation
Partial clone Reddit for evaluation Ionix

Reddit es un agregador de contenido determinado por la comunidad. Hablamos de una plataforma social en la que los usuarios envían publicaciones que otros usuarios pueden votar -a favor o en contra- según sus preferencias. Si una publicación recibe muchos votos, sube en la clasificación de Reddit y, por consiguiente, más gente puede verla.

Si recibe votos a la baja, su visibilidad se reduce rápidamente y desaparece de la vista de la mayoría de los usuarios. Reddit se divide en subcomunidades o subreddits. Cualquier usuario puede crear subreddits sobre cualquier tema, ya sea un asunto general, como tecnología, o específico, como una simple broma.

Cada subreddit pasa a formar parte de la lista completa de envíos de Reddit, lo cual significa que una publicación en cualquier subreddit (a menos que sea privada) puede llegar a la página principal del sitio web.

El proyecto IonixReddit de manera específica captura información de la plataforma sobre el contenido del país de Chile utilizando dos filtros, Shitposting e image. Cada tarjeta, resultado del filtro, contiene Título, Url de la imagen, Votación y número de comentarios.

Es una aplicación mobile para la tecnología Android.

Arquitectura Layers.

Los patrones de diseño se especifican en cada Layer y componente.

OBSERVACIONES A LAS ESPECIFICACIONES:

1.- Para el segundo punto se indica "Como en el primer punto, debes mostrar la misma información. ", pero no se especifica los filtros. Empezamos suponiendo que son igual al primer punto, es decir, link_flair_text: Shitposting y post_hint: image. En esta suposición cuando se utiliza la url de búsqueda: https://www.reddit.com/r/chile/search.json?q=(búsqueda)&limit=1000 casi no arroja ningún resultado, no satisfacen los dos filtros. En ese sentido hacer búsquedas termina siendo algo ocioso. la aplicación sólo utiliza el filtro post_hint: image para que pueda mostrar resultados de acuerdo a las especificaciones.

2.- Sobre la configuración y los permisos: se indica: "Necesitamos una vista de carga para la configuración necesaria (Splash) y una vista para solicitar al usuario permisos (Configuración) como cámara, escritura, gps, etc. los que gustes", de acuerdo a los requerimientos los permisos que exige esta oración no tienen sentido, permiso para camara...?, permiso para gps...?. En el desarrollo de software existen ciertos criterios muy validos, uno de ellos, es no añadir funcionalidad que no esta especificada. Métodos que no se utilizan, violentan el principio Interface Segregation Principle, SOLID, donde claramente indica que cada clase implementa las interfaces que necesite y use, NINGUNA MÁS. La aplicación sólo necesita permiso de acceso a INTERNET y al estado de la red.  

3.- Sobre "Manejo Online/Offline": para el punto dos no puede haber Offline porque el patrón de búsqueda es muy variable y hacemos consultas a una base de datos que no manejamos. Si la base de datos fuese nuestra es posible. Sobre qué criterios podríamos almacenar en nuestro repositorio, en el mejor de los casos sobre parámetros muy abstractos, tan así, que no podriamos saber cuales. En este sentido las consultas se realizan en tiempo real y necesariamente con conexión.

4.- Para finalizar espero una crítica detallada al igual que el punto "¿Qué tomamos en cuenta?", me parece que la discusión será muy provechosa. 
