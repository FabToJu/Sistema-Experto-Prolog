:- consult('hechos.pl'). 

:- dynamic borrado_logico/1.

% --- REGLA: borrado Lógico ---
cancion_activa(ID) :- 
    cancion(ID, _, _, _, _),
    not(borrado_logico(ID)).

% ====================================================
%  CLASIFICACIÓN DE ENERGÍA
% ====================================================

% Géneros de Alta Energía
genero_alta_energia(reggaeton).
genero_alta_energia(salsa).
genero_alta_energia(dance).
genero_alta_energia(rock_pesado).

% Géneros de Baja Energía
genero_baja_energia(lofi).
genero_baja_energia(clasica).
genero_baja_energia(acustico).
genero_baja_energia(chill).

% Géneros de Media Energía
genero_media_energia(pop).
genero_media_energia(jazz).
genero_media_energia(balada).

% ====================================================
%  BÚSQUEDAS RECURSIVAS EN LISTAS
% ====================================================

% Recursión para buscar alta energía
tiene_alta_energia([G | _]) :- genero_alta_energia(G), !. 
tiene_alta_energia([_ | Resto]) :- tiene_alta_energia(Resto).

% Recursión para buscar baja energía
tiene_baja_energia([G | _]) :- genero_baja_energia(G), !.
tiene_baja_energia([_ | Resto]) :- tiene_baja_energia(Resto).

% Recursión para buscar media energía
tiene_media_energia([G | _]) :- genero_media_energia(G), !.
tiene_media_energia([_ | Resto]) :- tiene_media_energia(Resto).


% ====================================================
% REGLAS DE INFERENCIA DE ENERGÍA
% ====================================================

% Energía Alta
inferir_energia(ID, alta) :- 
    cancion_activa(ID), 
    cancion(ID, _, _, Generos, _), 
    tiene_alta_energia(Generos).

% Energía Baja
inferir_energia(ID, baja) :- 
    cancion_activa(ID), 
    cancion(ID, _, _, Generos, _), 
    tiene_baja_energia(Generos).

% Energía Media (Por defecto)
inferir_energia(ID, media) :- 
    cancion_activa(ID), 
    cancion(ID, _, _, Generos, _), 
    tiene_media_energia(Generos),
    not(inferir_energia(ID, alta)), 
    not(inferir_energia(ID, baja)).
