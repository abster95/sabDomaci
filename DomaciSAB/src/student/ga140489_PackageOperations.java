/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import static java.lang.Math.sqrt;
import java.util.logging.Logger;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import operations.PackageOperations;
import util.DB;
import util.ga140489_Pair;

public class ga140489_PackageOperations implements PackageOperations {

    @Override
    public int insertPackage(int districtFrom, int districtTo, String userName, int packageType, BigDecimal weight) {

        try {
            //get user
            int KorisnikID, ZahtevZaPrevozID = 0;

            //get userID
            String sql = "SELECT KorisnikID FROM Korisnik WHERE KorisnickoIme=?";
            PreparedStatement pst = DB.getConnection().prepareStatement(sql);

            pst.setString(1, userName);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                KorisnikID = rs.getInt("KorisnikID");
            } else {
                return -1;
            }

            //insert request
            sql = "INSERT INTO ZahtevZaPrevoz(TipPaketa,Tezina,KorisnikID,IzOpstinaID,UOpstinaID) VALUES (?,?,?,?,?)";
            pst = DB.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pst.setInt(1, packageType);
            pst.setBigDecimal(2, weight);
            pst.setInt(3, KorisnikID);
            pst.setInt(4, districtFrom);
            pst.setInt(5, districtTo);

            if (pst.executeUpdate() <= 0) {

                return -1;
            }
            ResultSet keys = pst.getGeneratedKeys();
            if (keys.next()) {
                ZahtevZaPrevozID = keys.getInt(1);
            }
            //insert package

            sql = "INSERT INTO Paket(Status,Cena,VremePrihvatanja,KurirID,ZahtevZaPrevozID) VALUES (?,?,?,?,?)";
            pst = DB.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, 0);
            pst.setBigDecimal(2, BigDecimal.ZERO);
            pst.setTimestamp(3, null);
            pst.setObject(4, null);
            pst.setInt(5, ZahtevZaPrevozID);

            if (pst.executeUpdate() <= 0) {

                return -1;
            }
            keys = pst.getGeneratedKeys();
            if (keys.next()) {
                int PaketID = keys.getInt(1);
                if (PaketID <= 0) {
                    return -1;
                } else {
                    return PaketID;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ga140489_PackageOperations.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return -1;
    }

    @Override
    public int insertTransportOffer(String couriersUserName, int packageId, BigDecimal pricePercentage) {

        try {
            //if percentage = null
            if (pricePercentage == null) {
                BigDecimal range = new BigDecimal(10);
                BigDecimal rand = new BigDecimal(Math.random());
                BigDecimal randomBigDecimal = rand.divide(range, BigDecimal.ROUND_DOWN);

                pricePercentage = randomBigDecimal;
            }

            int KurirID = 0;

            //check if courier not driving status
            String sql = "SELECT k.Status AS Status, k.KurirID AS KurirID FROM Kurir k INNER JOIN Korisnik kor ON kor.KorisnikID=k.KorisnikID WHERE kor.KorisnickoIme=?";
            PreparedStatement pst = DB.getConnection().prepareStatement(sql);

            pst.setString(1, couriersUserName);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int status = rs.getInt("Status");
                KurirID = rs.getInt("KurirID");
                if (status != 0) {
                    return -1;
                }
            }

            //insert offer
            sql = "INSERT INTO Ponuda(Procenat,PaketID,KurirID) VALUES(?,?,?)";
            pst = DB.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setBigDecimal(1, pricePercentage);
            pst.setInt(2, packageId);
            pst.setInt(3, KurirID);

            if (pst.executeUpdate() <= 0) {
                return -1;
            }

            ResultSet nrs = pst.getGeneratedKeys();

            if (nrs.next()) {
                int key = nrs.getInt(1);
                return key;
            }

        } catch (SQLException ex) {
            Logger.getLogger(ga140489_PackageOperations.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }

        return -1;
    }

    private BigDecimal CalculatePrice(BigDecimal weight, BigDecimal distance, int type) {

        int osnovnaCena, tezinskiFaktor, CenaPoKg;

        if (type == 0) {
            osnovnaCena = 10;
            tezinskiFaktor = 0;
            CenaPoKg = 0;
        } else if (type == 1) {
            osnovnaCena = 25;
            tezinskiFaktor = 1;
            CenaPoKg = 100;
        } else {
            osnovnaCena = 75;
            tezinskiFaktor = 2;
            CenaPoKg = 300;
        }

        return weight.multiply(new BigDecimal(tezinskiFaktor)).multiply(new BigDecimal(CenaPoKg)).add(new BigDecimal(osnovnaCena)).multiply(distance);

    }

    private BigDecimal CalculateDistance(BigDecimal x1, BigDecimal y1, BigDecimal x2, BigDecimal y2) {

        BigDecimal A = x2.subtract(x1).multiply(x2.subtract(x1));
        BigDecimal B = y2.subtract(y1).multiply(y2.subtract(y1));

        return new BigDecimal(sqrt(A.doubleValue() + B.doubleValue()));
    }

    @Override
    public boolean acceptAnOffer(int offerId) {

        try {
            //read info from offer
            BigDecimal Procenat, Tezina, Cena, IzOpstinaXKord, IzOpstinaYKord, UOpstinaXKord, UOpstinaYKord;
            int PaketID, KurirID, ZahtevZaPrevozID, TipPaketa, KorisnikID;

            //get request
            String sql = "SELECT  pak.PaketID  AS PaketID, zzp.Tezina,zzp.TipPaketa, p.Procenat AS Procenat, p.KurirID, o1.XKord AS IzOpstinaXKord , o1.YKord AS IzOpstinaYKord, o2.XKord AS UOpstinaXKord, o2.YKord AS UOpstinaYKord , zzp.KorisnikID AS KorisnikID FROM Ponuda p INNER JOIN Paket pak ON p.PaketID=pak.PaketID \n"
                    + "INNER JOIN ZahtevZaPrevoz zzp ON pak.ZahtevZaPrevozID=zzp.ZahtevZaPrevozID\n"
                    + "INNER JOIN Opstina o1 ON o1.OpstinaID=zzp.IzOpstinaID\n"
                    + "INNER JOIN Opstina o2 ON o2.OpstinaID=zzp.UOpstinaID\n"
                    + "WHERE p.PonudaID=?";
            PreparedStatement pst = DB.getConnection().prepareStatement(sql);
            pst.setInt(1, offerId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                PaketID = rs.getInt("PaketID");
                Tezina = rs.getBigDecimal("Tezina");
                TipPaketa = rs.getInt("TipPaketa");
                Procenat = rs.getBigDecimal("Procenat");
                KurirID = rs.getInt("KurirID");
                IzOpstinaXKord = rs.getBigDecimal("IzOpstinaXKord");
                IzOpstinaYKord = rs.getBigDecimal("IzOpstinaYKord");
                UOpstinaXKord = rs.getBigDecimal("UOpstinaXKord");
                UOpstinaYKord = rs.getBigDecimal("UOpstinaYKord");
                KorisnikID = rs.getInt("KorisnikID");
            } else {
                return false;
            }

            //update package
            Cena = CalculatePrice(Tezina, CalculateDistance(IzOpstinaXKord, IzOpstinaYKord, UOpstinaXKord, UOpstinaYKord), TipPaketa);

            Cena = Procenat.multiply(Cena).divide(new BigDecimal(100)).add(Cena);

            sql = "UPDATE Paket SET Status=? ,Cena=? ,VremePrihvatanja=? ,KurirID=? WHERE PaketID=?";
            pst = DB.getConnection().prepareStatement(sql);

            pst.setInt(1, 1);
            pst.setBigDecimal(2, Cena.setScale(6, RoundingMode.CEILING));
            pst.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            pst.setInt(4, KurirID);
            pst.setInt(5, PaketID);

            if (pst.executeUpdate() <= 0) {
                return false;
            }

            //Update User number of packages sent
            sql = "UPDATE Korisnik SET BrojPoslatihPaketa=BrojPoslatihPaketa+1 WHERE KorisnikID=?";
            pst = DB.getConnection().prepareStatement(sql);
            pst.setInt(1, KorisnikID);

            return pst.executeUpdate() > 0;

        } catch (SQLException ex) {
            Logger.getLogger(ga140489_PackageOperations.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }

        return false;

    }

    @Override
    public List<Integer> getAllOffers() {
        List<Integer> result = new ArrayList<>();

        try {

            String sql = "SELECT PonudaID FROM Ponuda";
            Statement st = DB.getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                result.add(rs.getInt("PonudaID"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(ga140489_PackageOperations.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return result;
    }

    @Override
    public List<Pair<Integer, BigDecimal>> getAllOffersForPackage(int packageId) {
        List<Pair<Integer, BigDecimal>> result = new ArrayList<>();

        try {

            String sql = "SELECT PonudaID,Procenat FROM Ponuda WHERE PaketID=?";
            Statement st = DB.getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                int PonudaID = rs.getInt("PonudaID");
                BigDecimal Procenat = rs.getBigDecimal("Procenat");
                result.add(new ga140489_Pair<Integer, BigDecimal>(PonudaID, Procenat));

            }

        } catch (SQLException ex) {
            Logger.getLogger(ga140489_PackageOperations.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return result;
    }

    @Override
    public boolean deletePackage(int packageId) {

        try {
            int ZahtevZaPrevozID;
            //read info
            String sql = "SELECT ZahtevZaPrevozID FROM Paket WHERE PaketID=?";
            PreparedStatement pst = DB.getConnection().prepareStatement(sql);
            pst.setInt(1, packageId);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                ZahtevZaPrevozID = rs.getInt("ZahtevZaPrevozID");
            } else {
                return false;
            }

            sql = "DELETE FROM Paket WHERE PaketID=?";
            pst = DB.getConnection().prepareStatement(sql);
            pst.setInt(1, packageId);

            if (pst.executeUpdate() <= 0) {
                return false;
            }

            sql = "DELETE FROM ZahtevZaPrevoz WHERE ZahtevZaPrevozID=?";
            pst = DB.getConnection().prepareStatement(sql);
            pst.setInt(1, ZahtevZaPrevozID);

            if (pst.executeUpdate() > 0) {
                return true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(ga140489_PackageOperations.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return false;
    }

    @Override
    public boolean changeWeight(int packageId, BigDecimal newWeight) {

        try {
            //get request

            int ZahtevZaPrevozID;
            String sql = "SELECT * FROM Paket WHERE PaketID=?";

            PreparedStatement pst = DB.getConnection().prepareStatement(sql);

            pst.setInt(1, packageId);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                ZahtevZaPrevozID = rs.getInt("ZahtevZaPrevozID");
            } else {
                return false;
            }

            //update weight
            sql = "UPDATE ZahtevZaPrevoz(Tezina) VALUES (?) WHERE ZahtevZaPrevozID=?";
            pst = DB.getConnection().prepareStatement(sql);
            pst.setBigDecimal(1, newWeight);
            pst.setInt(2, ZahtevZaPrevozID);
            return pst.executeUpdate() > 0;

        } catch (SQLException ex) {
            Logger.getLogger(ga140489_PackageOperations.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return false;
    }

    @Override
    public boolean changeType(int packageId, int newType) {

        try {
            //get request

            int ZahtevZaPrevozID;
            String sql = "SELECT * FROM Paket WHERE PaketID=?";

            PreparedStatement pst = DB.getConnection().prepareStatement(sql);

            pst.setInt(1, packageId);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                ZahtevZaPrevozID = rs.getInt("ZahtevZaPrevozID");
            } else {
                return false;
            }

            //update weight
            sql = "UPDATE ZahtevZaPrevoz(TipPaketa) VALUES (?) WHERE ZahtevZaPrevozID=?";
            pst = DB.getConnection().prepareStatement(sql);
            pst.setInt(1, newType);
            pst.setInt(2, ZahtevZaPrevozID);
            return pst.executeUpdate() > 0;

        } catch (SQLException ex) {
            Logger.getLogger(ga140489_PackageOperations.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return false;
    }

    @Override
    public Integer getDeliveryStatus(int packageId) {

        try {
            String sql = "SELECT Status FROM Paket WHERE PaketID=?";
            PreparedStatement pst = DB.getConnection().prepareStatement(sql);
            pst.setInt(1, packageId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getInt("Status");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ga140489_PackageOperations.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public BigDecimal getPriceOfDelivery(int packageId) {

        try {
            if (getDeliveryStatus(packageId) == 0) {
                return null;
            }
            String sql = "SELECT Cena FROM Paket WHERE PaketID=?";
            PreparedStatement pst = DB.getConnection().prepareStatement(sql);
            pst.setInt(1, packageId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getBigDecimal("Cena");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ga140489_PackageOperations.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public Date getAcceptanceTime(int packageId) {

        try {
            String sql = "SELECT VremePrihvatanja FROM Paket WHERE PaketID=?";
            PreparedStatement pst = DB.getConnection().prepareStatement(sql);
            pst.setInt(1, packageId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getDate("VremePrihvatanja");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ga140489_PackageOperations.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public List<Integer> getAllPackagesWithSpecificType(int type) {
        List<Integer> result = new ArrayList<>();

        try {

            String sql = "SELECT pak.PaketID AS PaketID FROM Paket pak INNER JOIN ZahtevZaPrevoz zzp ON zzp.ZahtevZaPrevozID=pak.ZahtevZaPrevozID WHERE zzp.TipPaketa=?";
            PreparedStatement st = DB.getConnection().prepareStatement(sql);
            st.setInt(1, type);
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                result.add(rs.getInt("PaketID"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(ga140489_PackageOperations.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return result;
    }

    @Override
    public List<Integer> getAllPackages() {

        List<Integer> result = new ArrayList<>();

        try {

            String sql = "SELECT PaketID FROM Paket";
            Statement st = DB.getConnection().createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                result.add(rs.getInt("PaketID"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(ga140489_PackageOperations.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return result;
    }

    @Override
    public List<Integer> getDrive(String courierUsername) {
        List<Integer> result = new ArrayList<>();
        try {
            String sql = "SELECT p.PaketID as PaketID FROM Paket p \n"
                    + "INNER JOIN Kurir k ON k.KurirID=p.KurirID\n"
                    + "INNER JOIN Korisnik kor ON kor.KorisnikID=k.KorisnikID\n"
                    + "WHERE kor.KorisnickoIme=? AND (p.Status IN (1,2))\n"
                    + "ORDER BY p.VremePrihvatanja";

            PreparedStatement pst = DB.getConnection().prepareStatement(sql);

            pst.setString(1, courierUsername);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                result.add(rs.getInt("PaketID"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ga140489_PackageOperations.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return (result.size() > 0) ? result : null;
    }

    @Override
    public int driveNextPackage(String courierUsername) {

        try {
            //check if its first or last
            //
            int Status, BrojPaketa, KurirID, TipGoriva;
            BigDecimal Potrosnja;

            //get status
            String sql = "SELECT k.KurirID as KurirID, k.Status AS Status, v.TipGoriva AS TipGoriva, v.Potrosnja AS Potrosnja FROM Kurir k\n"
                    + "INNER JOIN Korisnik kor ON kor.KorisnikID=k.KorisnikID\n"
                    + "INNER JOIN Vozilo v ON k.VoziloID=v.VoziloID\n"
                    + "WHERE kor.KorisnickoIme=?";

            PreparedStatement pst = DB.getConnection().prepareStatement(sql);
            pst.setString(1, courierUsername);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                KurirID = rs.getInt("KurirID");
                Status = rs.getInt("Status");
                TipGoriva = rs.getInt("TipGoriva");
                Potrosnja = rs.getBigDecimal("Potrosnja");
            } else {
                return -2;
            }

            //get count
            List<Integer> result = getDrive(courierUsername);
            BrojPaketa = result.size();

            //
            if (BrojPaketa <= 0) {
                return -1;
            } else if (Status == 0) {

                //set courier status
                sql = "UPDATE Kurir SET Status=1 WHERE KurirID=?";
                pst = DB.getConnection().prepareStatement(sql);
                pst.setInt(1, KurirID);

                if (pst.executeUpdate() <= 0) {
                    return -2;
                }

            }
            //get next package to drive            

            int PaketID = result.get(0);

            int SledeciPaketID = 0;

            sql = "UPDATE Paket SET Status=3 WHERE PaketID=?";
            pst = DB.getConnection().prepareStatement(sql);
            pst.setInt(1, PaketID);
            pst.executeUpdate();

            //set next to taken
            if (BrojPaketa >= 2) {
                SledeciPaketID = result.get(1);

                sql = "UPDATE Paket SET Status=2 WHERE PaketID=?";
                pst = DB.getConnection().prepareStatement(sql);
                pst.setInt(1, SledeciPaketID);
                pst.executeUpdate();
            }
            //set profit

            //get distances
            BigDecimal IzOpstineX, IzOpstineY, UOpstinuX, UOpstinuY, SledeciIzOpstineX = null, SledeciIzOpstineY = null, CenaPaketa;

            sql = "SELECT o1.XKord AS IzOpstineX, o1.YKord AS IzOpstineY, o2.XKord AS UOpstinuX, o2.YKord AS UOpstinuY, p.Cena AS Cena FROM Paket p \n"
                    + "INNER JOIN ZahtevZaPrevoz zzp ON zzp.ZahtevZaPrevozID=p.ZahtevZaPrevozID \n"
                    + "INNER JOIN Opstina o1 ON o1.OpstinaID=zzp.IzOpstinaID\n"
                    + "INNER JOIN Opstina o2 ON o2.OpstinaID=zzp.UOpstinaID\n"
                    + "WHERE PaketID=?";
            pst = DB.getConnection().prepareStatement(sql);
            pst.setInt(1, PaketID);
            rs = pst.executeQuery();

            if (rs.next()) {
                IzOpstineX = rs.getBigDecimal("IzOpstineX");
                IzOpstineY = rs.getBigDecimal("IzOpstineY");
                UOpstinuX = rs.getBigDecimal("UOpstinuX");
                UOpstinuY = rs.getBigDecimal("UOpstinuY");
                CenaPaketa = rs.getBigDecimal("Cena");
            } else {
                return -2;
            }

            if (BrojPaketa > 1) {
                //if not last package

                sql = "SELECT o1.XKord AS IzOpstineX, o1.YKord AS IzOpstineY FROM Paket p \n"
                        + "INNER JOIN ZahtevZaPrevoz zzp ON zzp.ZahtevZaPrevozID=p.ZahtevZaPrevozID \n"
                        + "INNER JOIN Opstina o1 ON o1.OpstinaID=zzp.IzOpstinaID\n"
                        + "INNER JOIN Opstina o2 ON o2.OpstinaID=zzp.UOpstinaID\n"
                        + "WHERE PaketID=?";
                pst = DB.getConnection().prepareStatement(sql);
                pst.setInt(1, SledeciPaketID);
                rs = pst.executeQuery();

                if (rs.next()) {
                    SledeciIzOpstineX = rs.getBigDecimal("IzOpstineX");
                    SledeciIzOpstineY = rs.getBigDecimal("IzOpstineY");
                } else {
                    return -2;
                }
            }

            //read courier profit
            BigDecimal Profit;
            BigDecimal distance;
            BigDecimal PotrosnjaVoznje;
            int PotrosnjaPoKm;

            if (TipGoriva == 0) {
                PotrosnjaPoKm = 15;
            } else if (TipGoriva == 1) {
                PotrosnjaPoKm = 36;
            } else {
                PotrosnjaPoKm = 32;
            }
            distance = CalculateDistance(IzOpstineX, IzOpstineY, UOpstinuX, UOpstinuY);

            PotrosnjaVoznje = distance.multiply(new BigDecimal(PotrosnjaPoKm)).multiply(Potrosnja);

            Profit = CenaPaketa.subtract(PotrosnjaVoznje);

            //check for next Package if got to pick up to other place
            if (result.size() >= 2) {
                if (!(SledeciIzOpstineX.equals(UOpstinuX) && SledeciIzOpstineY.equals(UOpstinuY))) {
                    BigDecimal SledecaDistanca = CalculateDistance(UOpstinuX, UOpstinuY, SledeciIzOpstineX, SledeciIzOpstineY);
                    PotrosnjaVoznje = SledecaDistanca.multiply(new BigDecimal(PotrosnjaPoKm)).multiply(Potrosnja);
                    Profit = Profit.subtract(PotrosnjaVoznje);
                }

            }

            sql = "UPDATE Kurir SET Profit=Profit+? , BrojPaketa=BrojPaketa+1 WHERE KurirID=?";
            pst = DB.getConnection().prepareStatement(sql);
            pst.setBigDecimal(1, Profit.setScale(6, RoundingMode.CEILING));
            pst.setInt(2, KurirID);

            if (pst.executeUpdate() <= 0) {
                return -2;
            }

            if ((Status == 1) && (BrojPaketa == 1)) {
                sql = "UPDATE Kurir SET Status=0 WHERE KurirID=?";
                pst = DB.getConnection().prepareStatement(sql);
                pst.setInt(1, KurirID);

                pst.executeUpdate();
            }

            return PaketID;
        } catch (SQLException ex) {
            Logger.getLogger(ga140489_PackageOperations.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }

        return -1;

    }

}
