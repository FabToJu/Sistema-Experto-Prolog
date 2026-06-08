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

restaurar_logica(ID) :-
    retractall(borrado_logico(ID)).

% --- Agregar Hechos Físicos (En Memoria) ---
agregar_cancion(ID, Titulo, Artista, Generos, Idioma) :-
    assertz(cancion(ID, Titulo, Artista, Generos, Idioma)).

% --- Borrado Físico (En Memoria) ---
% Elimina completamente el hecho de la memoria temporal.
eliminar_fisica(ID) :-
    retractall(cancion(ID, _, _, _, _)).

% (Opcional) --- Guardar Cambios en Disco ---
% Sobrescribe hechos.pl con los hechos actuales en memoria.
guardar_hechos_en_disco :-
    tell('../conocimiento/hechos.pl'),
    write('% ==================== HECHOS ===================='), nl,
    write('% Archivo auto-generado por guardado fisico'), nl,
    write(':- dynamic cancion/5.'), nl, nl,
    listing(cancion/5),
    told.
