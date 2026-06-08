:- consult('../conocimiento/reglas_actividad.pl').
:- consult('../conocimiento/reglas_idioma.pl').
:- consult('../estado/sesion.pl').

% ====================================================
% API PARA EL CLIENTE JAVA 
% ====================================================

% ----------------------------------------------------
% ENDPOINT 1: Búsqueda por Actividad
% Uso en Java: Query q = new Query("buscar_por_actividad('estudiar', Titulo, Artista, GenerosStr)");
% ----------------------------------------------------
buscar_por_actividad(Actividad, Titulo, Artista, GenerosStr) :-
    cancion_activa(ID),
    apta_para_actividad(ID, Actividad),
    cancion(ID, Titulo, Artista, Generos, _),
    atomics_to_string(Generos, ', ', GenerosStr).

% ----------------------------------------------------
% ENDPOINT 2: Búsquedapor Emoción
% Uso en Java: Query q = new Query("buscar_por_emocion('feliz', Titulo, Artista, GenerosStr)");
% ----------------------------------------------------
buscar_por_emocion(Emocion, Titulo, Artista, GenerosStr) :-
    cancion_activa(ID),
    inferir_emocion(ID, Emocion),
    cancion(ID, Titulo, Artista, Generos, _),
    atomics_to_string(Generos, ', ', GenerosStr).

% ----------------------------------------------------
% ENDPOINT 3: Búsqueda por Energía e Idioma
% Uso en Java: Query q = new Query("buscar_por_idioma_energia('alta', 'espanol', Titulo, Artista, GenerosStr)");
% ----------------------------------------------------
buscar_por_idioma_energia(Energia, Idioma, Titulo, Artista, GenerosStr) :-
    cancion_activa(ID),
    (Energia == cualquiera ; inferir_energia(ID, Energia)),
    compatible_idioma(ID, Idioma),
    cancion(ID, Titulo, Artista, Generos, _),
    atomics_to_string(Generos, ', ', GenerosStr).

% ----------------------------------------------------
% ENDPOINT 4: Agregar una nueva canción
% Uso: Query q = new Query("api_agregar_cancion('c_31', 'When I Was Your Man', 'Bruno Mars', ['balada', 'pop'], 'ingles')");
% ----------------------------------------------------
api_agregar_cancion(ID, Titulo, Artista, Generos, Idioma) :-
    agregar_cancion(ID, Titulo, Artista, Generos, Idioma).

% ----------------------------------------------------
% ENDPOINT 5: Eliminar una canción (Físico o Lógico)
% Parámetro Tipo: 'fisico' o 'logico'
% Uso: Query q = new Query("api_eliminar_cancion('c_01', logico)");
% ----------------------------------------------------
api_eliminar_cancion(ID, logico) :- eliminar_logica(ID).
api_eliminar_cancion(ID, fisico) :- eliminar_fisica(ID).
api_eliminar_cancion(ID, archivo) :- eliminar_fisica_archivo(ID).

% ----------------------------------------------------
% ENDPOINT 6: Restaurar canción (Quitar borrado lógico)
% Uso: Query q = new Query("api_restaurar_cancion('c_01')");
% ----------------------------------------------------
api_restaurar_cancion(ID) :- restaurar_cancion(ID).

% ----------------------------------------------------
% ENDPOINT 7: Obtener todas las canciones activas
% ----------------------------------------------------
api_obtener_todas_canciones(ID, Titulo, Artista, GenerosStr, Idioma) :-
    cancion_activa(ID),
    cancion(ID, Titulo, Artista, Generos, Idioma),
    atomics_to_string(Generos, ', ', GenerosStr).

% ----------------------------------------------------
% ENDPOINT 8: Consultar canción
% ----------------------------------------------------
api_consultar(ID, Titulo, Artista, GenerosStr, Idioma) :-
    cancion_activa(ID),
    cancion(ID, Titulo, Artista, Generos, Idioma),
    atomics_to_string(Generos, ', ', GenerosStr).
