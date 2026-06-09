:- consult('reglas_energia.pl').

% ====================================================
% REGLAS DE COMPATIBILIDAD DE IDIOMA
% ====================================================

% 1. Coincidencia exacta con el idioma
compatible_idioma(ID, IdiomaBuscado) :- 
    cancion_activa(ID), 
    cancion(ID, _, _, _, IdiomaBuscado).

% 2. La música instrumental es compatible 
% con cualquier búsqueda.
compatible_idioma(ID, _) :- 
    cancion_activa(ID), 
    cancion(ID, _, _, _, instrumental).
