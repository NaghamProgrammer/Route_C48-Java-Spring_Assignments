import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        LibraryItem.setLibraryName("Bayt Al Hekma Library");
        LibraryItem.setAdministrativeCharge(10.0);

        Library library = new Library(10, 10);



        library.registerItem(new Book("B001", "Clean Code", "Robert C. Martin", 464));
        library.registerItem(new Book("B002", "The Pragmatic Programmer", "Andrew Hunt", 352));
        library.registerItem(new Magazine("M001", "AI Monthly", 25));
        library.registerItem(new Magazine("M002", "Science Today", 40));
        library.registerItem(new DVD("D001", "Introduction to Java", 120));



        library.registerMember(new Member("Nagham", "S001", MembershipType.STUDENT));
        library.registerMember(new Member("Ahmed", "S002", MembershipType.STAFF));
        library.registerMember(new Member("Mariam", "S003", MembershipType.PUBLIC));



        boolean running = true;

        while (running) {

            byte choice = menu();

            switch (choice) {

                case 1:
                    viewCatalogue(library);
                    break;

                case 2:
                    registerMember(library);
                    break;

                case 3:
                    borrowItem(library);
                    break;

                case 4:
                    returnItem(library);
                    break;

                case 5:
                    renewLoan(library);
                    break;

                case 6:
                    searchItem(library);
                    break;

                case 7:
                    viewItemsByStatus(library);
                    break;

                case 8:
                    payOutstandingFine(library);
                    break;

                case 9:
                    viewAllMembers(library);
                    break;

                case 10:
                    libraryReport(library);
                    break;

                case 0:
                    running = false;
                    System.out.println("\nThank you for using "
                            + LibraryItem.getLibraryName() + ".");
                    break;
            }
        }

        sc.close();
    }


    // =====================================================
    // MENU
    // =====================================================

    static byte menu() {

        byte choice;

        while (true) {

            System.out.println();
            System.out.println("========================================");
            System.out.println("        " + LibraryItem.getLibraryName());
            System.out.println("========================================");
            System.out.println("1. View catalogue");
            System.out.println("2. Register member");
            System.out.println("3. Borrow item");
            System.out.println("4. Return item");
            System.out.println("5. Renew loan");
            System.out.println("6. Search item by ID");
            System.out.println("7. View items by status");
            System.out.println("8. Pay outstanding fines");
            System.out.println("9. View all members");
            System.out.println("10. Library report");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            if (!sc.hasNextByte()) {
                System.out.println("Invalid choice.");
                sc.next();
                continue;
            }

            choice = sc.nextByte();
            sc.nextLine();

            if (choice >= 0 && choice <= 10) {
                return choice;
            }

            System.out.println("Invalid choice.");
        }
    }

    static void viewCatalogue(Library library) {

        System.out.println("\n========== CATALOGUE ==========");

        library.listWholeCatalogue();
    }


    static void registerMember(Library library) {

        System.out.println("\n========== REGISTER MEMBER ==========");

        System.out.print("Enter member name: ");
        String name = sc.nextLine();

        System.out.print("Enter membership ID: ");
        String membershipId = sc.nextLine();

        System.out.println("Membership categories:");
        System.out.println("1. Student (25% fine waiver)");
        System.out.println("2. Staff (10% fine waiver)");
        System.out.println("3. Public (0% fine waiver)");

        System.out.print("Choose category: ");

        int categoryChoice;

        while (!sc.hasNextInt()) {
            System.out.println("Invalid category.");
            sc.next();
            System.out.print("Choose category: ");
        }

        categoryChoice = sc.nextInt();
        sc.nextLine();

        MembershipType category;

        switch (categoryChoice) {

            case 1:
                category = MembershipType.STUDENT;
                break;

            case 2:
                category = MembershipType.STAFF;
                break;

            case 3:
                category = MembershipType.PUBLIC;
                break;

            default:
                System.out.println("Invalid category.");
                return;
        }

        Member member = new Member(name, membershipId, category);

        if (library.registerMember(member)) {

            System.out.println("Member registered successfully.");

        } else {

            System.out.println(
                    "Could not register member. The register may be full or the ID may already exist"
            );
        }
    }


    static void borrowItem(Library library) {

        System.out.println("\n========== BORROW ITEM ==========");

        System.out.print("Enter catalogue ID: ");
        String catalogueId = sc.nextLine();

        System.out.print("Enter membership ID: ");
        String membershipId = sc.nextLine();

        LibraryItem item = library.findItemByCatalogueId(catalogueId);

        Member member = library.findMemberByMembershipId(membershipId);

        if (item == null) {
            System.out.println("Item not found.");
            return;
        }

        if (member == null) {
            System.out.println("Member not found.");
            return;
        }

        if (library.lendItem(catalogueId, membershipId)) {

            System.out.println("Item borrowed successfully.");
            System.out.println(
                    "Loan period: " + item.getLoanPeriod() + " days."
            );

        } else {

            System.out.println(
                    "Borrowing failed. The item may not be available or the member may not be eligible"
            );
        }
    }


    static void returnItem(Library library) {

        System.out.println("\n========== RETURN ITEM ==========");

        System.out.print("Enter catalogue ID: ");
        String catalogueId = sc.nextLine();

        System.out.print("Enter number of days overdue: ");

        if (!sc.hasNextInt()) {
            System.out.println("Invalid number of days");
            sc.next();
            sc.nextLine();
            return;
        }

        int daysOverdue = sc.nextInt();
        sc.nextLine();

        if (daysOverdue < 0) {
            System.out.println("Days overdue cannot be negative");
            return;
        }

        LibraryItem item =
                library.findItemByCatalogueId(catalogueId);

        if (item == null) {
            System.out.println("Item not found");
            return;
        }

        if (item.getStatus() != ItemStatus.ON_LOAN) {
            System.out.println("This item is not currently on loan");
            return;
        }

        if (library.returnItem(catalogueId, daysOverdue)) {
            System.out.println("Item returned successfully");
        } else {
            System.out.println("Return failed");
        }
    }


    static Member findMemberForReturn(Library library, String borrowerName) {
        return null;
    }


    static void renewLoan(Library library) {

        System.out.println("\n========== RENEW LOAN ==========");

        System.out.print("Enter catalogue ID: ");
        String catalogueId = sc.nextLine();

        LibraryItem item =
                library.findItemByCatalogueId(catalogueId);

        if (item == null) {
            System.out.println("Item not found.");
            return;
        }

        if (!(item instanceof Renewable)) {

            System.out.println(item.getCategory() + " items cannot be renewed.");

            return;
        }

        Renewable renewable = (Renewable) item;

        if (renewable.renewLoan()) {

            int renewalsUsed = item.getRenewalCount();

            int renewalLimit = renewable.reportRenewalLimit();

            System.out.println("Loan renewed successfully.");

            System.out.println("Renewals remaining: " + (renewalLimit - renewalsUsed));

        } else {

            System.out.println(
                    "Renewal failed. The item may not be " + "on loan or may have reached its renewal limit."
            );
        }
    }



    static void searchItem(Library library) {

        System.out.println("\n========== SEARCH ITEM ==========");

        System.out.print("Enter catalogue ID: ");
        String catalogueId = sc.nextLine();

        LibraryItem item =
                library.findItemByCatalogueId(catalogueId);

        if (item == null) {

            System.out.println("Item not found.");

        } else {

            item.display();
        }
    }



    static void viewItemsByStatus(Library library) {

        System.out.println("\n========== VIEW ITEMS BY STATUS ==========");

        System.out.println("1. AVAILABLE");
        System.out.println("2. ON_LOAN");
        System.out.println("3. RESERVED");
        System.out.println("4. LOST");

        System.out.print("Choose status: ");

        if (!sc.hasNextInt()) {
            System.out.println("Invalid status.");
            sc.next();
            sc.nextLine();
            return;
        }

        int choice = sc.nextInt();
        sc.nextLine();

        ItemStatus status;

        switch (choice) {

            case 1:
                status = ItemStatus.AVAILABLE;
                break;

            case 2:
                status = ItemStatus.ON_LOAN;
                break;

            case 3:
                status = ItemStatus.RESERVED;
                break;

            case 4:
                status = ItemStatus.LOST;
                break;

            default:
                System.out.println("Invalid status");
                return;
        }

        library.listItemsFilteredByState(status);
    }



    static void payOutstandingFine(Library library) {

        System.out.println("\n========== PAY OUTSTANDING FINE ==========");

        System.out.print("Enter membership ID: ");
        String membershipId = sc.nextLine();

        Member member = library.findMemberByMembershipId(membershipId);

        if (member == null) {

            System.out.println("Member not found.");
            return;
        }

        System.out.printf(
                "Current balance: %.2f EGP%n",
                member.getBalanceOwed()
        );

        System.out.print("Enter amount to pay: ");

        if (!sc.hasNextDouble()) {

            System.out.println("Invalid amount");
            sc.next();
            sc.nextLine();
            return;
        }

        double amount = sc.nextDouble();
        sc.nextLine();

        if (amount <= 0) {

            System.out.println(
                    "Payment must be greater than zero."
            );

            return;
        }

        if (amount > member.getBalanceOwed()) {

            System.out.println(
                    "Payment cannot exceed the balance owed."
            );

            return;
        }

        if (member.payFine(amount)) {

            System.out.printf(
                    "Payment successful.%n" + "New balance: %.2f EGP%n",
                    member.getBalanceOwed()
            );

        } else {

            System.out.println("Payment failed.");
        }
    }


    static void viewAllMembers(Library library) {

        System.out.println("\n========== ALL MEMBERS ==========");

        library.listAllMembersWithOwings();
    }


    static void libraryReport(Library library) {

        System.out.println("\n========== LIBRARY REPORT ==========");

        library.displayReport();
    }
}