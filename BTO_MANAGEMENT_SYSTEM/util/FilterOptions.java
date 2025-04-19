// ===========================
// File: util/FilterOptions.java
// ===========================
package util;

import java.util.Scanner;
import project.BTOProject;

public class FilterOptions {
    private int priceCeiling = 0;
    private boolean onlyShowAvailable = false;
    public static final String SORT_ALPHABETICAL = "alphabetical";
    public static final String SORT_BY_DATE = "date";
    private String neighborhoodFilter;
    private String flatTypeFilter;
    private String sortOrder;

    public FilterOptions() {
        this.neighborhoodFilter = "";
        this.flatTypeFilter = "";
        this.sortOrder = SORT_ALPHABETICAL;
        this.priceCeiling = 0;
        this.onlyShowAvailable = false;
    }

    public FilterOptions(String neighborhoodFilter, String flatTypeFilter, String sortOrder) {
        this.neighborhoodFilter = neighborhoodFilter;
        this.flatTypeFilter = flatTypeFilter;
        this.sortOrder = sortOrder;
        this.priceCeiling = 0;
        this.onlyShowAvailable = false;
    }

    public void promptUpdate(Scanner sc) {
        System.out.println("Current filters:");
        System.out.println("- Neighborhood: " + (getNeighborhoodFilter().isEmpty() ? "(None)" : getNeighborhoodFilter()));
        System.out.println("- Flat Type: " + (getFlatTypeFilter().isEmpty() ? "(None)" : getFlatTypeFilter()));
        System.out.println("- Max Price: " + (getPriceCeiling() == 0 ? "(None)" : "$" + getPriceCeiling()));
        System.out.println("- Only Available Units: " + (isOnlyShowAvailable() ? "Yes" : "No"));

        System.out.print("Would you like to update filters? (yes/no): ");
        String input = sc.nextLine();
        if (input.equalsIgnoreCase("yes")) {
            System.out.print("Enter neighborhood filter (leave blank for none): ");
            setNeighborhoodFilter(sc.nextLine());
            System.out.print("Enter flat type filter (leave blank for none): ");
            setFlatTypeFilter(sc.nextLine());
            System.out.print("Enter max price (0 for no limit): ");
            try {
                setPriceCeiling(Integer.parseInt(sc.nextLine()));
            } catch (NumberFormatException e) {
                setPriceCeiling(0);
            }
            System.out.print("Show only projects with available units? (yes/no): ");
            setOnlyShowAvailable(sc.nextLine().equalsIgnoreCase("yes"));
        }
    }

    public boolean matches(BTOProject p) {
        boolean neighborhoodMatch = neighborhoodFilter.isEmpty() ||
            p.getNeighborhood().toLowerCase().contains(neighborhoodFilter.toLowerCase());

        boolean flatMatch = flatTypeFilter.isEmpty() ||
            p.getType1().equalsIgnoreCase(flatTypeFilter) ||
            p.getType2().equalsIgnoreCase(flatTypeFilter);

        boolean priceMatch = priceCeiling == 0 ||
            (p.getPriceType1() <= priceCeiling || p.getPriceType2() <= priceCeiling);

        boolean availabilityMatch = !onlyShowAvailable ||
            (p.getUnitsType1() > 0 || p.getUnitsType2() > 0);

        return neighborhoodMatch && flatMatch && priceMatch && availabilityMatch;
    }

    public String getNeighborhoodFilter() {
        return neighborhoodFilter;
    }

    public void setNeighborhoodFilter(String neighborhoodFilter) {
        this.neighborhoodFilter = neighborhoodFilter;
    }

    public String getFlatTypeFilter() {
        return flatTypeFilter;
    }

    public void setFlatTypeFilter(String flatTypeFilter) {
        this.flatTypeFilter = flatTypeFilter;
    }

    public int getPriceCeiling() {
        return priceCeiling;
    }

    public void setPriceCeiling(int priceCeiling) {
        this.priceCeiling = priceCeiling;
    }

    public boolean isOnlyShowAvailable() {
        return onlyShowAvailable;
    }

    public void setOnlyShowAvailable(boolean onlyShowAvailable) {
        this.onlyShowAvailable = onlyShowAvailable;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}