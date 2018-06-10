
CREATE TABLE [Admin]
( 
	[KorisnikID]         integer  NOT NULL 
)
go

ALTER TABLE [Admin]
	ADD CONSTRAINT [XPKAdmin] PRIMARY KEY  CLUSTERED ([KorisnikID] ASC)
go

CREATE TABLE [Grad]
( 
	[GradID]             integer  IDENTITY  NOT NULL ,
	[PostanskiBroj]      varchar(20)  NULL ,
	[Naziv]              varchar(20)  NULL 
)
go

ALTER TABLE [Grad]
	ADD CONSTRAINT [XPKGrad] PRIMARY KEY  CLUSTERED ([GradID] ASC)
go

CREATE TABLE [Korisnik]
( 
	[KorisnikID]         integer  IDENTITY  NOT NULL ,
	[Ime]                varchar(20)  NULL ,
	[Prezime]            varchar(20)  NULL ,
	[KorisnickoIme]      varchar(20)  NULL ,
	[Lozinka]            varchar(20)  NULL ,
	[TipKorisnika]       integer  NULL ,
	[PoslatiPaketi]      integer  NULL 
)
go

ALTER TABLE [Korisnik]
	ADD CONSTRAINT [XPKKorisnik] PRIMARY KEY  CLUSTERED ([KorisnikID] ASC)
go

CREATE TABLE [Kurir]
( 
	[Profit]             decimal(10,3)  NULL ,
	[Status]             integer  NULL ,
	[BrojPaketa]         integer  NULL ,
	[VoziloID]           integer  NULL ,
	[KorisnikID]         integer  NOT NULL 
)
go

ALTER TABLE [Kurir]
	ADD CONSTRAINT [XPKKurir] PRIMARY KEY  CLUSTERED ([KorisnikID] ASC)
go

CREATE TABLE [Opstina]
( 
	[OpstinaID]          integer  IDENTITY  NOT NULL ,
	[Naziv]              varchar(20)  NULL ,
	[XKord]              decimal(10,3)  NULL ,
	[YKord]              decimal(10,3)  NULL ,
	[GradID]             integer  NULL 
)
go

ALTER TABLE [Opstina]
	ADD CONSTRAINT [XPKOpstina] PRIMARY KEY  CLUSTERED ([OpstinaID] ASC)
go

CREATE TABLE [Paket]
( 
	[PaketID]            integer  IDENTITY  NOT NULL ,
	[Status]             integer  NULL ,
	[Cena]               decimal(10,3)  NULL ,
	[VremePrihvatanja]   datetime  NULL ,
	[ZahtevZaPrevozID]   integer  NULL ,
	[KorisnikID]         integer  NULL 
)
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [XPKPaket] PRIMARY KEY  CLUSTERED ([PaketID] ASC)
go

CREATE TABLE [Ponuda]
( 
	[PonudaID]           integer  IDENTITY  NOT NULL ,
	[Procenat]           decimal(10,3)  NULL ,
	[PaketID]            integer  NULL ,
	[KorisnikID]         integer  NULL 
)
go

ALTER TABLE [Ponuda]
	ADD CONSTRAINT [XPKPonuda] PRIMARY KEY  CLUSTERED ([PonudaID] ASC)
go

CREATE TABLE [Vozilo]
( 
	[VoziloID]           integer  IDENTITY  NOT NULL ,
	[RegBroj]            varchar(20)  NULL ,
	[TipGoriva]          integer  NULL ,
	[Potrosnja]          decimal(10,3)  NULL 
)
go

ALTER TABLE [Vozilo]
	ADD CONSTRAINT [XPKVozilo] PRIMARY KEY  CLUSTERED ([VoziloID] ASC)
go

CREATE TABLE [ZahtevZaKurira]
( 
	[ZahtevZaKuriraID]   integer  IDENTITY  NOT NULL ,
	[VoziloID]           integer  NULL ,
	[KorisnikID]         integer  NULL 
)
go

ALTER TABLE [ZahtevZaKurira]
	ADD CONSTRAINT [XPKZahtevZaKurira] PRIMARY KEY  CLUSTERED ([ZahtevZaKuriraID] ASC)
go

CREATE TABLE [ZahtevZaPrevoz]
( 
	[ZahtevZaPrevozID]   integer  IDENTITY  NOT NULL ,
	[TipPaketa]          integer  NULL ,
	[Tezina]             decimal(10,3)  NULL ,
	[KorisnikID]         integer  NULL ,
	[IzOpstinaID]        integer  NULL ,
	[UOpstinaID]         integer  NULL 
)
go

ALTER TABLE [ZahtevZaPrevoz]
	ADD CONSTRAINT [XPKZahtevZaPrevoz] PRIMARY KEY  CLUSTERED ([ZahtevZaPrevozID] ASC)
go


ALTER TABLE [Admin]
	ADD CONSTRAINT [R_18] FOREIGN KEY ([KorisnikID]) REFERENCES [Korisnik]([KorisnikID])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go


ALTER TABLE [Kurir]
	ADD CONSTRAINT [R_3] FOREIGN KEY ([VoziloID]) REFERENCES [Vozilo]([VoziloID])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Kurir]
	ADD CONSTRAINT [R_17] FOREIGN KEY ([KorisnikID]) REFERENCES [Korisnik]([KorisnikID])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go


ALTER TABLE [Opstina]
	ADD CONSTRAINT [R_1] FOREIGN KEY ([GradID]) REFERENCES [Grad]([GradID])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Paket]
	ADD CONSTRAINT [R_16] FOREIGN KEY ([ZahtevZaPrevozID]) REFERENCES [ZahtevZaPrevoz]([ZahtevZaPrevozID])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [R_9] FOREIGN KEY ([KorisnikID]) REFERENCES [Kurir]([KorisnikID])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Ponuda]
	ADD CONSTRAINT [R_10] FOREIGN KEY ([PaketID]) REFERENCES [Paket]([PaketID])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Ponuda]
	ADD CONSTRAINT [R_11] FOREIGN KEY ([KorisnikID]) REFERENCES [Kurir]([KorisnikID])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [ZahtevZaKurira]
	ADD CONSTRAINT [R_8] FOREIGN KEY ([VoziloID]) REFERENCES [Vozilo]([VoziloID])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [ZahtevZaKurira]
	ADD CONSTRAINT [R_7] FOREIGN KEY ([KorisnikID]) REFERENCES [Kurir]([KorisnikID])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go


ALTER TABLE [ZahtevZaPrevoz]
	ADD CONSTRAINT [R_12] FOREIGN KEY ([KorisnikID]) REFERENCES [Korisnik]([KorisnikID])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [ZahtevZaPrevoz]
	ADD CONSTRAINT [R_13] FOREIGN KEY ([IzOpstinaID]) REFERENCES [Opstina]([OpstinaID])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [ZahtevZaPrevoz]
	ADD CONSTRAINT [R_15] FOREIGN KEY ([UOpstinaID]) REFERENCES [Opstina]([OpstinaID])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go
