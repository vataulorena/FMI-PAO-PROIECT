Sistem de Gestionare a Aplicatiei de Muzica

Prezentare generală

Sistemul de Gestionare a Muzicii este o aplicație bazată pe Java, concepută pentru a administra biblioteci muzicale, playlist-uri utilizator și controlul accesului utilizatorilor. Este construită cu componente de backend care gestionează datele, autentificarea utilizatorilor și executarea comenzilor folosind baze de date SQL.

Funcționalități

Gestionarea utilizatorilor

- Autentificare: Utilizatorii se pot înregistra, autentifica și deconecta. Parolele sunt stocate în mod sigur în baza de date.
- Autorizare: Utilizatorii pot fi promovați la statutul de administrator, oferindu-le privilegii suplimentare.

Gestionarea bibliotecii muzicale

- Managementul cântecelor: Administratorii pot adăuga cântece noi în bibliotecă, inclusiv detalii precum titlul, artistul și anul lansării.
- Managementul playlist-urilor: Utilizatorii pot crea, modifica și gestiona playlist-uri personale adăugând cântece din bibliotecă.

Funcționalitatea de căutare

- Căutare cântece: Utilizatorii pot căuta cântece după titlu sau artist.

Exportul datelor

- Exportul playlist-urilor: Utilizatorii pot exporta playlist-urile în formatele CSV sau JSON, facilitând partajarea sau backup-ul datelor.

Jurnalizarea auditului

- Jurnalizarea comenzilor: Toate comenzile utilizatorilor sunt înregistrate cu rezultatele lor, ajutând la depanare și auditurile de securitate.

Comenzi

- login: Autentificarea unui utilizator.
- register: Înregistrarea unui utilizator nou.
- logout: Deconectarea utilizatorului curent.
- promote: Promovarea unui utilizator standard la administrator.
- create song: Adăugarea unui cântec nou în biblioteca muzicală.
- create playlist: Crearea unui playlist nou.
- list playlists: Listarea tuturor playlist-urilor unui utilizator.
- add byname: Adăugarea de cântece într-un playlist specificând numele playlist-ului.
- add byid: Adăugarea de cântece într-un playlist specificând ID-ul playlist-ului.
- search: Căutarea de cântece după titlu sau artist.
- export playlist: Exportarea unui playlist într-un fișier în format CSV sau JSON.
- audit: Obținerea jurnalului de audit pentru comenzile utilizatorilor.
