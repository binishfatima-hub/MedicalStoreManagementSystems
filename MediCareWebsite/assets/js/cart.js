/* ============================================================
   cart.js  -  the shopping cart of the USER PANEL.

   The cart is only a temporary list kept in the browser
   (localStorage). Nothing is saved in the database until the
   user presses "Place Order" on the checkout page.

   The prices kept here are only for SHOWING the customer a
   total. When the order is placed the backend reads the real
   price and discount from MySQL again and recalculates
   everything, so a changed price in the browser has no effect.

   Each cart belongs to one user, so the key contains the
   user's email: medicareCart_ravi@gmail.com
   ============================================================ */

function cartKey() {

    const user = getUser();

    return "medicareCart_" + (user ? user.email : "guest");
}

/** Reads the cart. Returns [] when it is empty. */
function getCart() {

    try {
        const raw = localStorage.getItem(cartKey());
        return raw ? JSON.parse(raw) : [];

    } catch (error) {
        return [];
    }
}

function saveCart(items) {
    localStorage.setItem(cartKey(), JSON.stringify(items));
}

function clearCart() {
    localStorage.removeItem(cartKey());
}

/** How many different medicines are in the cart. */
function cartCount() {
    return getCart().length;
}

/**
 * Adds a medicine. If it is already in the cart the quantity
 * is increased instead of adding a second line.
 */
function addToCart(medicine, quantity) {

    const items = getCart();
    const wanted = Number(quantity) || 1;

    const existing = items.find(function (item) {
        return item.id === medicine.id;
    });

    if (existing) {
        existing.quantity = existing.quantity + wanted;

        // refresh the prices in case the admin changed them
        existing.price = medicine.price;
        existing.discountPercent = medicine.discountPercent;
        existing.finalPrice = medicine.finalPrice;

    } else {
        items.push({
            id: medicine.id,
            name: medicine.name,
            company: medicine.company,
            price: medicine.price,                     // MRP
            discountPercent: medicine.discountPercent, // % off
            finalPrice: medicine.finalPrice,           // what the customer pays
            quantity: wanted
        });
    }

    saveCart(items);
}

function updateCartQuantity(medicineId, quantity) {

    const items = getCart();

    const item = items.find(function (row) {
        return row.id === medicineId;
    });

    if (item) {
        item.quantity = Math.max(1, Number(quantity) || 1);
        saveCart(items);
    }
}

function removeFromCart(medicineId) {

    const items = getCart().filter(function (row) {
        return row.id !== medicineId;
    });

    saveCart(items);
}

/**
 * Adds up the cart, exactly the way the backend does it:
 *
 *   subtotal = MRP x quantity          (before discount)
 *   discount = subtotal - what is actually charged
 *   taxable  = subtotal - discount
 *   gst      = taxable x 5%
 *   total    = taxable + gst
 */
function cartTotals() {

    const GST_PERCENT = 5;

    let subtotal = 0;
    let taxable = 0;

    getCart().forEach(function (item) {

        const mrp = Number(item.price) || 0;
        const finalPrice = Number(item.finalPrice != null ? item.finalPrice : item.price) || 0;
        const quantity = Number(item.quantity) || 0;

        subtotal += mrp * quantity;
        taxable += finalPrice * quantity;
    });

    const discount = round(subtotal - taxable);
    const gst = round(taxable * GST_PERCENT / 100);

    return {
        subtotal: round(subtotal),
        discount: discount,
        taxable: round(taxable),
        gst: gst,
        total: round(taxable + gst),
        gstPercent: GST_PERCENT
    };
}

function round(value) {
    return Math.round(value * 100) / 100;
}

/** Writes the number next to the Cart link in the navbar. */
function refreshCartBadge() {

    const link = document.getElementById("cartLink");

    if (link) {
        link.textContent = "Cart (" + cartCount() + ")";
    }
}
