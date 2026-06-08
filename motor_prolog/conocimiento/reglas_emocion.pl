:- consult('reglas_energia.pl').

% ====================================================
%  BÚSQUEDAS RECURSIVAS EN LISTAS
% ====================================================
% Buscar Balada
contiene_balada([balada | _]) :- !.
contiene_balada([_ | Resto]) :- contiene_balada(Resto).

% Buscar Jazz
contiene_jazz([jazz | _]) :- !.
contiene_jazz([_ | Resto]) :- contiene_jazz(Resto).

% Buscar Acústico
contiene_acustico([acustico | _]) :- !.
contiene_acustico([_ | Resto]) :- contiene_acustico(Resto).

% Buscar Rock
contiene_rock([rock | _]) :- !.
contiene_rock([_ | Resto]) :- contiene_rock(Resto).

% Buscar Pop
contiene_pop([pop | _]) :- !.
contiene_pop([_ | Resto]) :- contiene_pop(Resto).

% Buscar Dance
contiene_dance([dance | _]) :- !.
contiene_dance([_ | Resto]) :- contiene_dance(Resto).

% Buscar Reggaeton
contiene_reggaeton([reggaeton | _]) :- !.
contiene_reggaeton([_ | Resto]) :- contiene_reggaeton(Resto).


% ====================================================
% REGLAS DE INFERENCIA DE EMOCIÓN
% ====================================================

% 1. Romántico: Balada o Jazz, pero SOLO si tienen letra (no instrumentales).
inferir_emocion(ID, romantico) :- 
    cancion_activa(ID), 
    cancion(ID, _, _, Generos, Idioma),
    (contiene_balada(Generos) ; contiene_jazz(Generos)),
    Idioma \== instrumental.

% 2. Nostálgico: Nace de combinar Rock con Balada, o Acústico.
inferir_emocion(ID, nostalgico) :- 
    cancion_activa(ID), 
    cancion(ID, _, _, Generos, _),
    (contiene_acustico(Generos) ; (contiene_rock(Generos), contiene_balada(Generos))).

% 3. Relajado: Cualquier canción instrumental que tenga energía baja o media.
inferir_emocion(ID, relajado) :- 
    cancion_activa(ID), 
    cancion(ID, _, _, _, instrumental),
    (inferir_energia(ID, baja) ; inferir_energia(ID, media)).

% 4. Feliz: Nace de la combinación de Pop con géneros bailables.
inferir_emocion(ID, feliz) :- 
    cancion_activa(ID), 
    cancion(ID, _, _, Generos, _),
    contiene_pop(Generos), 
    (contiene_dance(Generos) ; contiene_reggaeton(Generos)).

% 5. Energético: Alta energía pura, sin importar si es feliz o no.
inferir_emocion(ID, energetico) :- 
    inferir_energia(ID, alta),
    \+ inferir_emocion(ID, feliz). % Exclusión lógica: si es feliz, ya no lo etiquetamos como puramente "energético".

% 6. Triste: Balada o Acústico que no logró ser Nostálgico ni Romántico.
inferir_emocion(ID, triste) :- 
    cancion_activa(ID), 
    cancion(ID, _, _, Generos, _),
    (contiene_balada(Generos) ; contiene_acustico(Generos)),
    \+ inferir_emocion(ID, nostalgico),
    \+ inferir_emocion(ID, romantico).