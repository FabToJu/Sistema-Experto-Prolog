:- consult('../conocimiento/reglas_actividad.pl').
:- consult('../conocimiento/reglas_idioma.pl').

% ====================================================
% API PARA EL CLIENTE JAVA 
% ====================================================

% ----------------------------------------------------
% ENDPOINT 1: Búsqueda rápida por Actividad
% Uso en Java: Query q = new Query("buscar_por_actividad('estudiar', Titulo, Artista, GenerosStr)");
% ----------------------------------------------------
buscar_por_actividad(Actividad, Titulo, Artista, GenerosStr) :-
    cancion_activa(ID),
    apta_para_actividad(ID, Actividad),
    cancion(ID, Titulo, Artista, Generos, _),
    atomic_list_concat(Generos, ', ', GenerosStr).

% ----------------------------------------------------
% ENDPOINT 2: Búsqueda rápida por Emoción
% Uso en Java: Query q = new Query("buscar_por_emocion('feliz', Titulo, Artista, GenerosStr)");
% ----------------------------------------------------
buscar_por_emocion(Emocion, Titulo, Artista, GenerosStr) :-
    cancion_activa(ID),
    inferir_emocion(ID, Emocion),
    cancion(ID, Titulo, Artista, Generos, _),
    atomic_list_concat(Generos, ', ', GenerosStr).

% ----------------------------------------------------
% ENDPOINT 3: Búsqueda por Perfil Físico (Energía e Idioma)
% Uso en Java: Query q = new Query("buscar_por_perfil('alta', 'espanol', Titulo, Artista, GenerosStr)");
% ----------------------------------------------------
buscar_por_perfil(Energia, Idioma, Titulo, Artista, GenerosStr) :-
    cancion_activa(ID),
    inferir_energia(ID, Energia),
    compatible_idioma(ID, Idioma),
    cancion(ID, Titulo, Artista, Generos, _),
    atomic_list_concat(Generos, ', ', GenerosStr).

% ----------------------------------------------------
% ENDPOINT 4: Búsqueda Avanzada (Soporta comodín "cualquiera")
% Si el usuario no selecciona un filtro en la interfaz, Java debe 
% enviar la palabra 'cualquiera'. Prolog ignorará ese filtro automáticamente.
% Uso en Java: Query q = new Query("buscar_avanzado('cualquiera', 'fiesta', 'alta', 'cualquiera', T, A, G)");
% ----------------------------------------------------
buscar_avanzado(Emocion, Actividad, Energia, Idioma, Titulo, Artista, GenerosStr) :-
    cancion_activa(ID),
    
    % Evaluación condicional (if-else): 
    % Si el parámetro es 'cualquiera', pasa como 'true'. Si no, ejecuta la inferencia.
    (Emocion == cualquiera -> true ; inferir_emocion(ID, Emocion)),
    (Actividad == cualquiera -> true ; apta_para_actividad(ID, Actividad)),
    (Energia == cualquiera -> true ; inferir_energia(ID, Energia)),
    (Idioma == cualquiera -> true ; compatible_idioma(ID, Idioma)),
    
    cancion(ID, Titulo, Artista, Generos, _),
    atomic_list_concat(Generos, ', ', GenerosStr).

% ====================================================
% API DE ADMINISTRACIÓN (MANEJO DE ESTADO Y CRUD)
% ====================================================

% ----------------------------------------------------
% ENDPOINT 5: Agregar una nueva canción
% Uso: Query q = new Query("api_agregar_cancion('c_99', 'Nueva Cancion', 'Artista X', ['pop', 'dance'], 'espanol')");
% ----------------------------------------------------
api_agregar_cancion(ID, Titulo, Artista, Generos, Idioma) :-
    agregar_cancion(ID, Titulo, Artista, Generos, Idioma).

% ----------------------------------------------------
% ENDPOINT 6: Eliminar una canción (Físico o Lógico)
% Parámetro Tipo: 'fisico' o 'logico'
% Uso: Query q = new Query("api_eliminar_cancion('c_01', logico)");
% ----------------------------------------------------
api_eliminar_cancion(ID, logico) :- eliminar_logica(ID).
api_eliminar_cancion(ID, fisico) :- eliminar_fisica(ID).

% ----------------------------------------------------
% ENDPOINT 7: Restaurar canción (Quitar borrado lógico)
% Uso: Query q = new Query("api_restaurar_cancion('c_01')");
% ----------------------------------------------------
api_restaurar_cancion(ID) :- restaurar_logica(ID).

% ----------------------------------------------------
% ENDPOINT 8: Guardar cambios físicos en disco
% Uso: Query q = new Query("api_guardar_cambios");
% ----------------------------------------------------
api_guardar_cambios :- guardar_hechos_en_disco.