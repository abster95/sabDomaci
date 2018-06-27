CREATE TRIGGER TR_Transport_Offer_Delete
    ON dbo.Paket
    FOR UPDATE
    AS
    BEGIN
    SET NOCOUNT ON
	DECLARE @Paket int
	SELECT @Paket = PaketID FROM inserted

	DELETE FROM Ponuda WHERE PaketID=@Paket

    END
