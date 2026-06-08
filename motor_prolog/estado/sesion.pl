:- consult('../conocimiento/hechos.pl').

% ====================================================
% ESTADO DE SESIÓN Y MANEJO DE CONOCIMIENTO
% ====================================================

% Declaración de hechos dinámicos en memoria
:- dynamic borrado_logico/1.

% --- REGLA: Canción Activa ---
% Evalúa que la canción exista y no esté marcada como eliminada lógicamente.
cancion_activa(ID) :- 
    cancion(ID, _, _, _, _),
    not(borrado_logico(ID)).

% ====================================================
% OPERACIONES CRUD (FÍSICO Y LÓGICO)
% ====================================================

% --- Borrado Lógico ---
% Marca una canción como eliminada en la memoria temporal de la sesión.
eliminar_logica(ID) :-
    assertz(borrado_logico(ID)).

restaurar_cancion(ID) :-
    retractall(borrado_logico(ID)).

% --- Agregar Hechos Físicos (En Memoria) ---
agregar_cancion(ID, Titulo, Artista, Generos, Idioma) :-
    assertz(cancion(ID, Titulo, Artista, Generos, Idioma)),
    append('motor_prolog/conocimiento/hechos.pl'),
    nl,
    writeq(cancion(ID, Titulo, Artista, Generos, Idioma)),
    write('.'), told.
    

% --- Borrado Físico (En Memoria) ---
% Elimina completamente el hecho de la memoria temporal.
eliminar_fisica(ID) :-
    retractall(cancion(ID, _, _, _, _)),
    restaurar_cancion(ID).

eliminar_fisica_archivo(ID) :-
    retractall(cancion(ID, _, _, _, _)),
    tell('motor_prolog/conocimiento/hechos.pl'),
    listing(cancion/5),
    told.
