package ispy.corp.moneywzrd.investments;
//ISpy Corp


//setting the Strings for the sort options
public class FavInfo {
    String favsym;
    String favprice;
    String favchange;


    FavInfo(String str, String price, String change) {
        this.favsym = str;
        this.favprice = price;
        this.favchange = change;
    }
}
