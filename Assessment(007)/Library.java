public class Library {

    private LibraryItem[] catalogue;
    private Member[] members;

    private int catalogueCount;
    private int membersCount;


    public Library(int catalogueCapacity, int membersCapacity) {
        this.catalogue = new LibraryItem[catalogueCapacity];
        this.members = new Member[membersCapacity];

        this.catalogueCount = 0;
        this.membersCount = 0;
    }


    //========================REGISTERING THINGS===============================

    //refuse registering if catalogue is full or if id is taken
    public boolean registerItem(LibraryItem item){

        if(catalogueCount >= catalogue.length || findItemByCatalogueId(item.getCatalogueId()) != null){
            return false;
        }

        catalogue[catalogueCount] = item;
        catalogueCount++;


        return true;

    }

    public boolean registerMember(Member member) {

        if (membersCount >= members.length || findMemberByMembershipId(member.getMembershipId()) != null) {
            return false;
        }

        members[membersCount] = member;
        membersCount++;

        return true;
    }

    //========================FINDING THINGS===============================
    public LibraryItem findItemByCatalogueId(String catalogueId){
        for(int i = 0; i < catalogueCount; i++){
            if(catalogue[i].getCatalogueId().equals(catalogueId)){
                return catalogue[i];
            }
        }
        return null;
    }


    public Member findMemberByMembershipId(String membershipId){
        for(int i = 0; i < membersCount; i++){
            if(members[i].getMembershipId().equals(membershipId)){
                return members[i];
            }
        }
        return null;
    }


    //========================LISTING THINGS===============================
    public void listWholeCatalogue(){
        for(int i = 0; i < catalogueCount; i++){
            catalogue[i].display();
        }
    }
    public void listItemsFilteredByState(ItemStatus itemStatus) {

        boolean found = false;

        for (int i = 0; i < catalogueCount; i++) {

            if (catalogue[i].getStatus() == itemStatus) {
                catalogue[i].display();
                found = true;
            }
        }

        if (!found) {
            System.out.println("No items found with this status");
        }
    }

    public void listAllMembersWithOwings(){
        for(int i = 0; i < membersCount; i++){
            members[i].display();
        }
    }
    //========================CALCULATING THINGS===========================
    public int countItemsOnLoan(){
        int count = 0;
        for(int i = 0; i < catalogueCount; i++){
            if(catalogue[i].getStatus() == ItemStatus.ON_LOAN){
                count++;
            }
        }
        return count;
    }

    public double calculateLoanRate(){

        if (catalogueCount == 0) {
            return 0.0;
        }

        return ((double) countItemsOnLoan() / catalogueCount) * 100;

    }

    public double calculateAllMembersBalance(){
        double total = 0.0;
        for(int i = 0; i < membersCount; i++){
            total += members[i].getBalanceOwed();
        }
        return total;
    }

    public double calculateProjectedFines(int daysOverdue) {

        double total = 0.0;

        for (int i = 0; i < catalogueCount; i++) {

            if (catalogue[i].getStatus() == ItemStatus.ON_LOAN) {
                total += catalogue[i].calculateFine(daysOverdue);
            }
        }

        return total;
    }

    //============================OPERATIONS===============================

    public boolean lendItem(String catalogueId, String membershipId) {

        LibraryItem item = findItemByCatalogueId(catalogueId);
        Member member = findMemberByMembershipId(membershipId);


        //item or member doesn't exist
        if (item == null || member == null) {
            return false;
        }


        //item must be available
        if (item.getStatus() != ItemStatus.AVAILABLE) {
            return false;
        }


        //member must be eligible
        if (!member.isEligibleToBorrow()) {
            return false;
        }


        //perform the loan
        if (item.lend(member.getName())) {

            member.recordBorrow();

            return true;
        }

        return false;
    }

    public boolean returnItem(String catalogueId, int daysOverdue) {

        LibraryItem item = findItemByCatalogueId(catalogueId);

        //item doesn't exist
        if (item == null) {
            return false;
        }

        //days overdue cannot be negative
        if (daysOverdue < 0) {
            return false;
        }

        //item must currently be on loan
        if (item.getStatus() != ItemStatus.ON_LOAN) {
            return false;
        }


        Member member = findMemberByName(item.getBorrowerName());

        if (member == null) {
            return false;
        }

        double baseFine = item.calculateFine(daysOverdue);

        double waiver = baseFine * member.getCategory().getWaiverRate();

        double waivedFine = baseFine - waiver;

        double administrativeCharge = 0.0;

        if (daysOverdue > 0) {
            administrativeCharge = LibraryItem.getAdministrativeCharge();
        }

        double finalAmount = waivedFine + administrativeCharge;

        System.out.println("\n---------- FINE BREAKDOWN ----------");
        System.out.println("Member: " + member.getName());
        System.out.println("Item: " + item.getTitle());
        System.out.println("Days overdue: " + daysOverdue);

        System.out.printf("Base fine: %.2f EGP%n", baseFine);
        System.out.printf("Waiver: %.2f EGP%n", waiver);
        System.out.printf("Fine after waiver: %.2f EGP%n", waivedFine);
        System.out.printf("Administrative charge: %.2f EGP%n",
                administrativeCharge);
        System.out.printf("Total charged: %.2f EGP%n", finalAmount);


        if (finalAmount > 0) {
            member.chargeFine(finalAmount);
        }


        member.recordReturn();

        item.bringBack();

        return true;
    }


    //helper used when returning an item
    private Member findMemberByName(String name) {

        for (int i = 0; i < membersCount; i++) {

            if (members[i].getName().equals(name)) {
                return members[i];
            }
        }

        return null;
    }



    public boolean renewItem(String catalogueId) {

        LibraryItem item = findItemByCatalogueId(catalogueId);


        //item doesn't exist
        if (item == null) {
            return false;
        }


        //check whether this item has renewal capability
        if (!(item instanceof Renewable)) {
            return false;
        }


        Renewable renewableItem = (Renewable) item;

        return renewableItem.renewLoan();
    }

    //=============================REPORTING===============================
    public void displayReport() {
        System.out.println("Catalogue size: " + catalogueCount);
        System.out.println("Items ever catalogued: " + LibraryItem.getRunningCount());
        System.out.println("Items on loan: " + countItemsOnLoan());
        System.out.println("Loan rate: " + calculateLoanRate() + "%");
        System.out.println("Total outstanding: " + calculateAllMembersBalance() + " EGP");
        System.out.println("Projected five days fines: " + calculateProjectedFines(5) + " EGP");
    }




}
