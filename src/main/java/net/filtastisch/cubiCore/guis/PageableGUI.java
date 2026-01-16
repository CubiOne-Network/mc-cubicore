package net.filtastisch.cubiCore.guis;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PageableGUI extends GUI {
    private final List<GUIButton> items;
    private int currentPage;
    private final int itemsPerPage;

    public PageableGUI(String title, int rows) {
        super();
        this.items = new ArrayList<>();
        this.currentPage = 0;

        this.itemsPerPage = (rows - 1) * 9;
    }

    public PageableGUI addItem(GUIButton button) {
        items.add(button);
        return this;
    }

    public PageableGUI addItems(List<GUIButton> buttons) {
        items.addAll(buttons);
        return this;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return Math.max(1, (int) Math.ceil((double) items.size() / itemsPerPage));
    }

    public void nextPage() {
        if (currentPage < getTotalPages() - 1) {
            currentPage++;
            updatePage();
        }
    }

    public void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            updatePage();
        }
    }

    public void setPage(int page) {
        if (page >= 0 && page < getTotalPages()) {
            currentPage = page;
            updatePage();
        }
    }

    private void updatePage() {
        buttons.entrySet().removeIf(entry -> {
            if (toolbar != null) {
                int toolbarRow = toolbar.getToolbarRow();
                int slot = entry.getKey();
                return slot < toolbarRow * 9 || slot >= (toolbarRow + 1) * 9;
            }
            return true;
        });

        inventory.clear();

        int start = currentPage * itemsPerPage;
        int end = Math.min(start + itemsPerPage, items.size());

        for (int i = start; i < end; i++) {
            int slot = i - start;

            if (toolbar != null) {
                int toolbarRow = toolbar.getToolbarRow();
                if (slot / 9 >= toolbarRow) {
                    slot += 9;
                }
            }

            GUIButton button = items.get(i);
            buttons.put(slot, button);
            inventory.setItem(slot, button.getIcon());
        }

        renderToolbar();
    }

    @Override
    public void open(Player player) {
        updatePage();
        super.open(player);
    }

    public PageableGUI withDefaultNavigation() {
        int navRow = rows - 1;

        ToolbarConfig nav = new ToolbarConfig(navRow, new HashMap<>());

        ItemStack prevItem = new ItemStack(Material.ARROW);
        ItemMeta prevMeta = prevItem.getItemMeta();
        prevMeta.setDisplayName("§aVorherige Seite");
        prevItem.setItemMeta(prevMeta);

        nav.setButton(3, new GUIButton(prevItem).withListener((event, player) -> {
            previousPage();
        }));

        ItemStack pageItem = new ItemStack(Material.PAPER);
        ItemMeta pageMeta = pageItem.getItemMeta();
        pageMeta.setDisplayName("§eSeite " + (currentPage + 1) + " / " + getTotalPages());
        pageItem.setItemMeta(pageMeta);

        nav.setButton(4, new GUIButton(pageItem));

        ItemStack nextItem = new ItemStack(Material.ARROW);
        ItemMeta nextMeta = nextItem.getItemMeta();
        nextMeta.setDisplayName("§aNächste Seite");
        nextItem.setItemMeta(nextMeta);

        nav.setButton(5, new GUIButton(nextItem).withListener((event, player) -> {
            nextPage();
        }));

        this.withToolbar(nav);
        return this;
    }
}