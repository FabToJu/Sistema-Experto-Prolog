:- consult('reglas_energia.pl').

% ====================================================
% REGLAS DE INFERENCIA DE EMOCIÓN
% ====================================================

% 1. Romántico: Balada o Jazz, pero SOLO si tienen letra (no instrumentales).
inferir_emocion(ID, romantico) :- 
    cancion_activa(ID), 
    cancion(ID, _, _, Generos, Idioma),
    (member(balada, Generos) ; member(jazz, Generos)),
    Idioma \== instrumental.

% 2. Nostálgico: Nace de combinar Rock con Balada, o Acústico.
inferir_emocion(ID, nostalgico) :- 
    cancion_activa(ID), 
    cancion(ID, _, _, Generos, _),
    (member(acustico, Generos) ; (member(rock, Generos), member(balada, Generos))).

% 3. Relajado: Cualquier canción instrumental que tenga energía baja o media.
inferir_emocion(ID, relajado) :- 
    cancion_activa(ID), 
    cancion(ID, _, _, _, instrumental),
    (inferir_energia(ID, baja) ; inferir_energia(ID, media)).

% 4. Feliz: Nace de la combinación de Pop con géneros bailables.
inferir_emocion(ID, feliz) :- 
    cancion_activa(ID), 
    cancion(ID, _, _, Generos, _),
    member(pop, Generos), 
    (member(dance, Generos) ; member(reggaeton, Generos)).

% 5. Energético: Alta energía pura, sin importar si es feliz o no.
inferir_emocion(ID, energetico) :- 
    inferir_energia(ID, alta),
    \+ inferir_emocion(ID, feliz). % Exclusión lógica: si es feliz, ya no lo etiquetamos como puramente "energético".

% 6. Triste: Balada o Acústico que no logró ser Nostálgico ni Romántico.
inferir_emocion(ID, triste) :- 
    cancion_activa(ID), 
    cancion(ID, _, _, Generos, _),
    (member(balada, Generos) ; member(acustico, Generos)),
    \+ inferir_emocion(ID, nostalgico),
    \+ inferir_emocion(ID, romantico).
