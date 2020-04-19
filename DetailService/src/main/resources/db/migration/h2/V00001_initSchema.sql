-- DROP TABLE IF EXISTS public.abonentid CASCADE;
CREATE TABLE public.abonentid
(
    id uuid default random_uuid(),
    ctn VARCHAR(255),
    name VARCHAR(255),
    email VARCHAR(255),
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX abonentid_ctn_uindex ON public.abonentid (ctn);

-- DROP TABLE IF EXISTS public.sessions CASCADE;
CREATE TABLE public.sessions
(
    id int NOT NULL auto_increment,
    cell_id VARCHAR(255),
    ctn VARCHAR(255),
    CONSTRAINT sessions_abonentid_id_fk FOREIGN KEY (ctn) REFERENCES abonentid (ctn),
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX sessions_ctn_uuid_uindex ON public.sessions (cell_id, ctn);