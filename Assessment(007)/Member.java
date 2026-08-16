public class Member {
    private String name;
    private String membershipId; //no setter
    private MembershipType category ; //no setter
    private double balanceOwed;
    private int countItemsHeld;

    //constructor chaining to remove duplication
    public Member(String name, String membershipId, MembershipType category) {
        this(name , membershipId , category , 0.0);

    }

    public Member(String name, String membershipId, MembershipType category, double balanceOwed) {
        this.name = name;
        this.membershipId = membershipId;
        this.category = category;
        this.balanceOwed = balanceOwed;
        this.countItemsHeld = 0;
    }

    //getters for everything cause assignment said "report all of these"
    public String getName() {
        return name;
    }

    public String getMembershipId() {
        return membershipId;
    }

    public MembershipType getCategory() {
        return category;
    }

    public double getBalanceOwed() {
        return balanceOwed;
    }

    public int getCountItemsHeld() {
        return countItemsHeld;
    }


    //the part of the assignment saying "have its name corrected" probably means there is a setter for name
    public void setName(String name) {
        this.name = name;
    }


    //as for balance and count they can be changed through methods only
    public void chargeFine(double amount){
        if(amount > 0){
            balanceOwed += amount;
        }
    }

    public boolean payFine(double amount) {
        if (amount > 0 && amount <= balanceOwed) {
            balanceOwed -= amount;
            return true;
        }else{
            return false;
        }
    }

    public boolean isEligibleToBorrow() {
        return (balanceOwed <= 100) && (countItemsHeld < 3);
    }

    public void recordBorrow(){
        countItemsHeld++;
    }

    public void recordReturn() {
        if (countItemsHeld > 0) {
            countItemsHeld--;
        }
    }

    public void display(){
        System.out.println(
                name + " | " +
                        membershipId+ " | " +
                        category + " | " +
                        countItemsHeld+ " items held | " +
                        balanceOwed + " EGP owed"
        );
    }
}
