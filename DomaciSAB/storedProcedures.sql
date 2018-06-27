-- ================================================
-- Template generated from Template Explorer using:
-- Create Procedure (New Menu).SQL
--
-- Use the Specify Values for Template Parameters 
-- command (Ctrl-Shift-M) to fill in the parameter 
-- values below.
--
-- This block of comments will not be included in
-- the definition of the procedure.
-- ================================================
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE spOdobriZahtev
	-- Add the parameters for the stored procedure here
	@username varchar(20), 
	@status int OUTPUT
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;
	DECLARE @Vozilo int = -1
	DECLARE @Korisnik int = -1
	SELECT @Vozilo=z.VoziloID, @Korisnik=k.KorisnikID FROM
	dbo.Korisnik k INNER JOIN dbo.ZahtevZaKurira z ON k.KorisnikId = z.KorisnikID
	WHERE k.KorisnickoIme = @username

	IF(@Vozilo!= -1 AND @Korisnik!=-1)
	BEGIN
	DELETE FROM dbo.ZahtevZaKurira WHERE KorisnikID = @username
	INSERT INTO dbo.Kurir(KorisnikID, VoziloID, Profit, Status, BrojPaketa) 
	VALUES (@Korisnik, @Vozilo, 0, 0, 0)
	UPDATE Korisnik SET TipKorisnika = 3 WHERE KorisnikID = @Korisnik
	SET @status = 0
	END
	ELSE
	BEGIN
	SET @status = 1
	END
END
GO
