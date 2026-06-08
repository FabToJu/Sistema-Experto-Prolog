:- consult('reglas_emocion.pl').

% ====================================================
% REGLAS DE ADECUACIÓN DE ACTIVIDAD
% ====================================================

% 1. Ejercicio: Requiere energía alta para mantener el ritmo.
apta_para_actividad(ID, ejercicio) :- 
    cancion_activa(ID),
    inferir_energia(ID, alta).

% 2. Estudiar: Requiere enfoque. Energía baja o media. 
% Se prefiere música instrumental o que transmita relajación.
apta_para_actividad(ID, estudiar) :- 
    cancion_activa(ID),
    (inferir_energia(ID, baja) ; inferir_energia(ID, media)),
    (cancion(ID, _, _, _, instrumental) ; inferir_emocion(ID, relajado)).

% 3. Fiesta: Energía alta, y debe transmitir emociones positias.
% Filtramos el Rock pesado triste o baladas.
apta_para_actividad(ID, fiesta) :- 
    cancion_activa(ID),
    inferir_energia(ID, alta),
    (inferir_emocion(ID, feliz) ; inferir_emocion(ID, energetico)).

% 4. Dormir: Exige energía baja y ambiente relajado.
apta_para_actividad(ID, dormir) :- 
    cancion_activa(ID),
    inferir_energia(ID, baja),
    inferir_emocion(ID, relajado).

% 5. Conducir: Mantener la atención del conductor.
% Excluimos la energía baja.
apta_para_actividad(ID, conducir) :- 
    cancion_activa(ID),
    (inferir_energia(ID, media) ; inferir_energia(ID, alta)).

% 6. Trabajar: Un punto intermedio de concentración. 
% Idealmente energía media. Si es energía baja, está bien mientras 
% la canción no sea tan relajante que sirva para dormir.
apta_para_actividad(ID, trabajar) :- 
    cancion_activa(ID),
    inferir_energia(ID, media).
apta_para_actividad(ID, trabajar) :- 
    cancion_activa(ID),
    inferir_energia(ID, baja),
    \+ apta_para_actividad(ID, dormir).
