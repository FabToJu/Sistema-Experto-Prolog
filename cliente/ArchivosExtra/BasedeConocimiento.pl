
% ==================== BASE DE CONOCIMIENTO ====================
% Sistema Experto - Recomendador Musical
% Archivo principal unificado
% Formato cancion: cancion(ID, Titulo, Artista, [Generos], Idioma)

% ==================== HECHOS ====================

% Declaracion de hechos dinamicos
:- dynamic cancion/5.
:- dynamic borrado_logico/1.

% --- Canciones ---
cancion(c01, 'Tusa', 'Karol G', [reggaeton], espanol).
cancion(c02, 'Bailando', 'Enrique Iglesias', [pop, reggaeton, salsa], espanol).
cancion(c03, 'Despacito', 'Luis Fonsi', [pop, reggaeton], espanol).
cancion(c04, 'Shape of You', 'Ed Sheeran', [pop, dance], ingles).
cancion(c05, 'Morning Coffee', 'Lofi Maker', [lofi, chill], instrumental).
cancion(c06, 'Amor Eterno', 'Juan Gabriel', [balada, acustico], espanol).
cancion(c07, 'Lloraras', 'Oscar D''Leon', [salsa], espanol).
cancion(c08, 'En El Muelle De San Blas', 'Mana', [balada, rock], espanol).
cancion(c09, 'Someone Like You', 'Adele', [balada, pop, acustico], ingles).
cancion(c10, 'Autumn Leaves', 'Miles Davis', [jazz], instrumental).
cancion(c11, 'De Musica Ligera', 'Soda Stereo', [rock], espanol).
cancion(c12, 'Rayando El Sol', 'Mana', [rock, balada], espanol).
cancion(c13, 'Bohemian Rhapsody', 'Queen', [rock, pop, balada], ingles).
cancion(c14, 'Hotel California', 'The Eagles', [rock, acustico], ingles).
cancion(c15, 'Midnight Stroll', 'Lofi Chill', [lofi, chill], instrumental).
cancion(c16, 'Dakiti', 'Bad Bunny', [reggaeton], espanol).
cancion(c17, 'La Rebelion', 'Joe Arroyo', [salsa], espanol).
cancion(c18, 'Blinding Lights', 'The Weeknd', [pop, rock], ingles).
cancion(c19, 'Eye of the Tiger', 'Survivor', [rock, rock_pesado], ingles).
cancion(c20, 'Oye Como Va', 'Tito Puente', [salsa, jazz], instrumental).
cancion(c21, 'What A Wonderful World', 'Louis Armstrong', [jazz], ingles).
cancion(c22, 'Clair de Lune', 'Claude Debussy', [clasica], instrumental).
cancion(c23, 'Four Seasons Spring', 'Vivaldi', [clasica], instrumental).
cancion(c24, 'Lo-Fi Beats 1', 'ChilledCow', [lofi], instrumental).
cancion(c25, 'Chill Vibes', 'Lofi Girl', [lofi, chill], instrumental).
cancion(c26, 'Fly Me To The Moon', 'Frank Sinatra', [jazz], ingles).
cancion(c27, 'Perfect', 'Ed Sheeran', [balada], ingles).
cancion(c28, 'Nocturne Op 9 No 2', 'Chopin', [clasica], instrumental).
cancion(c29, 'Just The Way You Are', 'Bruno Mars', [pop], ingles).
cancion(c30, 'Symphony No 5', 'Beethoven', [clasica], instrumental).

cancion(c31, 'Noche de Entities', 'J Balvin', [reggaeton, pop], espanol).
cancion(c32, 'Mi Gente', 'J Balvin', [reggaeton, dance], espanol).
cancion(c33, 'Lean On', 'Major Lazer', [pop, dance], ingles).
cancion(c34, 'Pepas', 'Farruko', [reggaeton, dance], espanol).
cancion(c35, 'Titi Me Pregunto', 'Bad Bunny', [reggaeton], espanol).
cancion(c36, 'Efecto', 'Bad Bunny', [reggaeton, pop], espanol).
cancion(c37, 'Ojitos Lindos', 'Bad Bunny', [reggaeton, balada], espanol).
cancion(c38, 'El Mundo Es Mio', 'Maluma', [reggaeton], espanol).
cancion(c39, 'Hawai', 'Maluma', [pop, reggaeton], espanol).
cancion(c40, 'Chantaje', 'Shakira', [pop, reggaeton], espanol).

cancion(c41, 'Watermelon Sugar', 'Harry Styles', [pop], ingles).
cancion(c42, 'Levitating', 'Dua Lipa', [pop, dance], ingles).
cancion(c43, 'Bad Guy', 'Billie Eilish', [pop], ingles).
cancion(c44, 'Stay', 'Justin Bieber', [pop, balada], ingles).
cancion(c45, 'Peaches', 'Justin Bieber', [pop], ingles).
cancion(c46, 'Anti Hero', 'Taylor Swift', [pop], ingles).
cancion(c47, 'Shake It Off', 'Taylor Swift', [pop, dance], ingles).
cancion(c48, 'As It Was', 'Harry Styles', [pop, rock], ingles).
cancion(c49, 'Flowers', 'Miley Cyrus', [pop], ingles).
cancion(c50, 'Unholy', 'Sam Smith', [pop], ingles).

cancion(c51, 'Under The Bridge', 'Red Hot Chili Peppers', [rock, acustico], ingles).
cancion(c52, 'Californication', 'Red Hot Chili Peppers', [rock, balada], ingles).
cancion(c53, 'Wonderwall', 'Oasis', [rock, acustico], ingles).
cancion(c54, 'Creep', 'Radiohead', [rock, balada], ingles).
cancion(c55, 'Black', 'Pearl Jam', [rock, balada], ingles).
cancion(c56, 'Come As You Are', 'Nirvana', [rock], ingles).
cancion(c57, 'Everlong', 'Foo Fighters', [rock], ingles).
cancion(c58, 'Learn To Fly', 'Foo Fighters', [rock], ingles).
cancion(c59, 'Mr Brightside', 'The Killers', [rock], ingles).
cancion(c60, 'Sex On Fire', 'Kings Of Leon', [rock], ingles).

cancion(c61, 'Persiana Americana', 'Soda Stereo', [rock], espanol).
cancion(c62, 'Cuando Pase El Temblor', 'Soda Stereo', [rock], espanol).
cancion(c63, 'La Marca De Cain', 'Fito Paez', [rock, balada], espanol).
cancion(c64, 'Oye Mi Amor', 'Mana', [rock], espanol).
cancion(c65, 'Te Busque', 'Nelly Furtado', [pop, rock], espanol).
cancion(c66, 'Besos En Guerra', 'Morat', [pop, acustico], espanol).
cancion(c67, 'Sigueme Y Te Sigo', 'Alejandro Sanz', [pop, balada], espanol).
cancion(c68, 'Corazon Partio', 'Alejandro Sanz', [balada, acustico], espanol).
cancion(c69, 'La Tortura', 'Shakira', [pop, rock], espanol).
cancion(c70, 'Inevitable', 'Shakira', [pop, balada], espanol).

cancion(c71, 'La Camisa Negra', 'Juanes', [pop, rock], espanol).
cancion(c72, 'A Dios Le Pido', 'Juanes', [pop, balada], espanol).
cancion(c73, 'El Perdedor', 'Enrique Iglesias', [balada, pop], espanol).
cancion(c74, 'Hero', 'Enrique Iglesias', [balada, pop], ingles).
cancion(c75, 'Por Amarte', 'Enrique Iglesias', [balada], espanol).
cancion(c76, 'All Of Me', 'John Legend', [balada, jazz], ingles).
cancion(c77, 'A Thousand Years', 'Christina Perri', [balada, acustico], ingles).
cancion(c78, 'Thinking Out Loud', 'Ed Sheeran', [balada, pop], ingles).
cancion(c79, 'Photograph', 'Ed Sheeran', [balada, acustico], ingles).
cancion(c80, 'Let Her Go', 'Passenger', [balada, acustico], ingles).

cancion(c81, 'La Vie En Rose', 'Edith Piaf', [jazz, balada], espanol).
cancion(c82, 'Summertime', 'Ella Fitzgerald', [jazz], ingles).
cancion(c83, 'Feeling Good', 'Nina Simone', [jazz], ingles).
cancion(c84, 'The Girl From Ipanema', 'Stan Getz', [jazz], instrumental).
cancion(c85, 'My Favorite Things', 'John Coltrane', [jazz], instrumental).
cancion(c86, 'Blue Bossa', 'Joe Henderson', [jazz], instrumental).
cancion(c87, 'All Blues', 'Miles Davis', [jazz], instrumental).
cancion(c88, 'Misty', 'Erroll Garner', [jazz, balada], ingles).
cancion(c89, 'Round Midnight', 'Thelonious Monk', [jazz], instrumental).
cancion(c90, 'Waterfall', 'Bossa Nova Trio', [jazz, chill], instrumental).

cancion(c91, 'Tokyo Lofi', 'Lofi Dreams', [lofi, chill], instrumental).
cancion(c92, 'Sleepy Sunday', 'ChillHop Music', [lofi, chill], instrumental).
cancion(c93, 'Coffee And Rain', 'Lofi Girl', [lofi, chill], instrumental).
cancion(c94, 'Nostalgic Afternoon', 'Lofi Maker', [lofi, chill], instrumental).
cancion(c95, 'Study Beats Vol 1', 'ChilledCow', [lofi], instrumental).
cancion(c96, 'Peaceful Mind', 'Lofi Hip Hop', [lofi, chill], instrumental).
cancion(c97, 'Rainy Window', 'Lofi Dreams', [lofi, chill], instrumental).
cancion(c98, 'Late Night Drive', 'Lofi Girl', [lofi, chill], instrumental).
cancion(c99, 'Warm Coffee', 'ChillHop Music', [lofi, chill], instrumental).
cancion(c100, 'Sunday Morning', 'Lofi Hip Hop', [lofi, chill], instrumental).

cancion(c101, 'El Gran Varon', 'Willie Colon', [salsa], espanol).
cancion(c102, 'Buenaventura Y Fabricio', 'Willie Colon', [salsa], espanol).
cancion(c103, 'Que Alguien Me Diga', 'Marc Anthony', [salsa, balada], espanol).
cancion(c104, 'Vivir Mi Vida', 'Marc Anthony', [salsa], espanol).
cancion(c105, 'Flor Palida', 'Marc Anthony', [salsa, balada], espanol).
cancion(c106, 'Mi Mayor Venganza', 'Marc Anthony', [salsa], espanol).
cancion(c107, 'Lloviendo', 'Ruben Blades', [salsa], espanol).
cancion(c108, 'Plastico', 'Ruben Blades', [salsa], espanol).
cancion(c109, 'Quiereme Mucho', 'La India', [salsa], espanol).
cancion(c110, 'Ese Hombre', 'La India', [salsa, balada], espanol).

cancion(c111, 'Gymnopédie No 1', 'Erik Satie', [clasica], instrumental).
cancion(c112, 'Prelude In C Major', 'Bach', [clasica], instrumental).
cancion(c113, 'Moonlight Sonata', 'Beethoven', [clasica], instrumental).
cancion(c114, 'Turkish March', 'Mozart', [clasica], instrumental).
cancion(c115, 'Ave Maria', 'Schubert', [clasica], instrumental).
cancion(c116, 'Cannon In D', 'Pachelbel', [clasica], instrumental).
cancion(c117, 'Swan Lake', 'Tchaikovsky', [clasica], instrumental).
cancion(c118, 'Air On G String', 'Bach', [clasica, chill], instrumental).
% ==================== REGLAS ====================

% ====================================================
% ESTADO DE SESION
% ====================================================

% Cancion activa: existe y no esta marcada como eliminada logicamente
cancion_activa(ID) :-
    cancion(ID, _, _, _, _),
    not(borrado_logico(ID)).

% ====================================================
% CLASIFICACION DE ENERGIA
% ====================================================

% Generos de Alta Energia
genero_alta_energia(reggaeton).
genero_alta_energia(salsa).
genero_alta_energia(dance).
genero_alta_energia(rock_pesado).

% Generos de Baja Energia
genero_baja_energia(lofi).
genero_baja_energia(clasica).
genero_baja_energia(acustico).
genero_baja_energia(chill).

% Generos de Media Energia
genero_media_energia(pop).
genero_media_energia(jazz).
genero_media_energia(balada).
genero_media_energia(rock).

% Busqueda recursiva en listas
tiene_alta_energia([G | _]) :- genero_alta_energia(G), !.
tiene_alta_energia([_ | Resto]) :- tiene_alta_energia(Resto).

tiene_baja_energia([G | _]) :- genero_baja_energia(G), !.
tiene_baja_energia([_ | Resto]) :- tiene_baja_energia(Resto).

tiene_media_energia([G | _]) :- genero_media_energia(G), !.
tiene_media_energia([_ | Resto]) :- tiene_media_energia(Resto).

% Reglas de inferencia de energia
inferir_energia(ID, alta) :-
    cancion_activa(ID),
    cancion(ID, _, _, Generos, _),
    tiene_alta_energia(Generos).

inferir_energia(ID, baja) :-
    cancion_activa(ID),
    cancion(ID, _, _, Generos, _),
    tiene_baja_energia(Generos).

inferir_energia(ID, media) :-
    cancion_activa(ID),
    cancion(ID, _, _, Generos, _),
    tiene_media_energia(Generos),
    not(inferir_energia(ID, alta)),
    not(inferir_energia(ID, baja)).

% ====================================================
% REGLAS DE INFERENCIA DE EMOCION
% ====================================================

% 1. Romantico: Balada o Jazz con letra (no instrumental)
inferir_emocion(ID, romantico) :-
    cancion_activa(ID),
    cancion(ID, _, _, Generos, Idioma),
    (member(balada, Generos) ; member(jazz, Generos)),
    Idioma \== instrumental.

% 2. Nostalgico: Rock con Balada o Acustico
inferir_emocion(ID, nostalgico) :-
    cancion_activa(ID),
    cancion(ID, _, _, Generos, _),
    (member(acustico, Generos) ; (member(rock, Generos), member(balada, Generos))).

% 3. Relajado: Instrumental con energia baja o media
inferir_emocion(ID, relajado) :-
    cancion_activa(ID),
    cancion(ID, _, _, _, instrumental),
    (inferir_energia(ID, baja) ; inferir_energia(ID, media)).

% 4. Feliz: Pop con generos bailables
inferir_emocion(ID, feliz) :-
    cancion_activa(ID),
    cancion(ID, _, _, Generos, _),
    member(pop, Generos),
    (member(dance, Generos) ; member(reggaeton, Generos)).

% 5. Energetico: Alta energia pura sin ser feliz
inferir_emocion(ID, energetico) :-
    inferir_energia(ID, alta),
    \+ inferir_emocion(ID, feliz).

% 6. Triste: Balada o Acustico que no es Nostalgico ni Romantico
inferir_emocion(ID, triste) :-
    cancion_activa(ID),
    cancion(ID, _, _, Generos, _),
    (member(balada, Generos) ; member(acustico, Generos)),
    \+ inferir_emocion(ID, nostalgico),
    \+ inferir_emocion(ID, romantico).

% ====================================================
% REGLAS DE ADECUACION DE ACTIVIDAD
% ====================================================

% 1. Ejercicio: Energia alta
apta_para_actividad(ID, ejercicio) :-
    cancion_activa(ID),
    inferir_energia(ID, alta).

% 2. Estudiar: Energia baja o media, instrumental o relajada
apta_para_actividad(ID, estudiar) :-
    cancion_activa(ID),
    (inferir_energia(ID, baja) ; inferir_energia(ID, media)),
    (cancion(ID, _, _, _, instrumental) ; inferir_emocion(ID, relajado)).

% 3. Fiesta: Energia alta con emocion positiva
apta_para_actividad(ID, fiesta) :-
    cancion_activa(ID),
    inferir_energia(ID, alta),
    (inferir_emocion(ID, feliz) ; inferir_emocion(ID, energetico)).

% 4. Dormir: Energia baja y relajada
apta_para_actividad(ID, dormir) :-
    cancion_activa(ID),
    inferir_energia(ID, baja),
    inferir_emocion(ID, relajado).

% 5. Conducir: Energia media o alta
apta_para_actividad(ID, conducir) :-
    cancion_activa(ID),
    (inferir_energia(ID, media) ; inferir_energia(ID, alta)).

% 6. Trabajar: Energia media
apta_para_actividad(ID, trabajar) :-
    cancion_activa(ID),
    inferir_energia(ID, media).

% 6b. Trabajar: Energia baja pero no tan relajante como para dormir
apta_para_actividad(ID, trabajar) :-
    cancion_activa(ID),
    inferir_energia(ID, baja),
    \+ apta_para_actividad(ID, dormir).

% ====================================================
% REGLAS DE COMPATIBILIDAD DE IDIOMA
% ====================================================

% 1. Coincidencia exacta con el idioma buscado
compatible_idioma(ID, IdiomaBuscado) :-
    cancion_activa(ID),
    cancion(ID, _, _, _, IdiomaBuscado).

% 2. Instrumental es compatible con cualquier idioma
compatible_idioma(ID, _) :-
    cancion_activa(ID),
    cancion(ID, _, _, _, instrumental).

% ====================================================
% API PARA JAVA
% ====================================================

% ENDPOINT 1: Busqueda avanzada con 4 filtros
% Uso: buscar_avanzado(Emocion, Actividad, Energia, Idioma, T, A, G)
% Si un filtro no aplica enviar: cualquiera
buscar_avanzado(Emocion, Actividad, Energia, Idioma, Titulo, Artista, GenerosStr) :-
    cancion_activa(ID),
    (Emocion   == cualquiera -> true ; inferir_emocion(ID, Emocion)),
    (Actividad == cualquiera -> true ; apta_para_actividad(ID, Actividad)),
    (Energia   == cualquiera -> true ; inferir_energia(ID, Energia)),
    (Idioma    == cualquiera -> true ; compatible_idioma(ID, Idioma)),
    cancion(ID, Titulo, Artista, Generos, _),
    atomic_list_concat(Generos, ', ', GenerosStr).

% ENDPOINT 2: Agregar cancion en memoria
% Uso: api_agregar_cancion(ID, Titulo, Artista, [Generos], Idioma)
api_agregar_cancion(ID, Titulo, Artista, Generos, Idioma) :-
    assertz(cancion(ID, Titulo, Artista, Generos, Idioma)).

% ENDPOINT 3: Eliminar cancion de forma logica (solo sesion)
% Uso: api_eliminar_cancion(ID, logico)
api_eliminar_cancion(ID, logico) :-
    assertz(borrado_logico(ID)).

% ENDPOINT 4: Eliminar cancion de forma fisica (de memoria)
% Uso: api_eliminar_cancion(ID, fisico)
api_eliminar_cancion(ID, fisico) :-
    retractall(cancion(ID, _, _, _, _)).

% ENDPOINT 5: Restaurar cancion eliminada logicamente
% Uso: api_restaurar_cancion(ID)
api_restaurar_cancion(ID) :-
    retractall(borrado_logico(ID)).

% ENDPOINT 6: Guardar cambios fisicos en disco
% Reescribe el archivo con los hechos actuales en memoria
% Uso: api_guardar_cambios
api_guardar_cambios :-
    tell('src/ArchivosExtra/BasedeConocimiento.pl'),
    write('% ==================== HECHOS ===================='), nl,
    write(':- dynamic cancion/5.'), nl,
    write(':- dynamic borrado_logico/1.'), nl, nl,
    listing(cancion/5),
    told.

% ENDPOINT 7: Consulta simple por ID
% Verifica si una cancion existe y esta activa
% Uso: api_consultar(ID)
api_consultar(ID) :-
    cancion_activa(ID).
