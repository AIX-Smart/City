package com.aix.city.dummy;

import com.aix.city.core.data.City;
import com.aix.city.core.EditableEventListing;
import com.aix.city.core.data.Location;
import com.aix.city.core.data.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    public static EditableEventListing LISTING;

    public static City AACHEN = new City(1, "Aachen");

    public static Location GINBAR = new Location(75463, "GinBar");

    public static String[] LEFT_MENU_ELEMENTS = {"post", "bars", "restaurants"};

    public static String[] RIGHT_MENU_ELEMENTS = {"favorites", "commented", "own business"};

    public static Tag BAR_TAG = new Tag(7353, "Bar");

    public static Tag RESTAURANT_TAG = new Tag(322, "Restaurant");


    static {
        // Add 3 sample items.
        addItem(new DummyItem("1", "Item 1"));
        addItem(new DummyItem("2", "Item 2"));
        addItem(new DummyItem("3", "Item 3"));

        EditableEventListing listing = GINBAR.getPostListing();
        listing.createEvent("Post 1: Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatib"); // 140 character
        listing.createEvent("Post 2: Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa.");
        listing.createEvent("Post 3: Lorem ipsum dolor sit amet, ");
        listing.createEvent("Post 4: Lorem ipsum dolor sit amet, consectetuer adipiscing elit.");
        listing.createEvent("Post 5: Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatib");
        listing.createEvent("Post 6: ");
        listing.createEvent("Post 7: Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula ");
        listing.createEvent("Post 8: Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatib");

        LISTING = listing;
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public String id;
        public String content;

        public DummyItem(String id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
