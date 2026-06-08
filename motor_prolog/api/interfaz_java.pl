:- consult('../conocimiento/reglas_actividad.pl').

% ====================================================
% API PARA EL CLIENTE JAVA (MÚLTIPLES ENDPOINTS)
% ====================================================
% Este archivo expone varios predicados de búsqueda para que 
% el cliente Java pueda usarlos según la sección de su interfaz,
% evitando choques de filtros y simplificando las consultas.

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