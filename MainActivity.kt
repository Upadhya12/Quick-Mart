package com.example.e_commerce

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.e_commerce.ui.theme.EcommerceTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcommerceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavHost(navController)
                }
            }
        }
    }
}

private object Routes {
    const val Launch = "launch"
    const val Home = "home"
    const val Catalog = "catalog"
    const val Cart = "cart"
    const val Profile = "profile"
    const val Payment = "payment"
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.Launch
    ) {
        composable(Routes.Launch) {
            LaunchScreen(
                onFinished = {
                    navController.navigate(Routes.Home) {
                        popUpTo(Routes.Launch) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.Home) {
            MainScaffold(navController, selectedRoute = Routes.Home) {
                HomeScreen()
            }
        }
        composable(Routes.Catalog) {
            MainScaffold(navController, selectedRoute = Routes.Catalog) {
                CatalogScreen()
            }
        }
        composable(Routes.Cart) {
            MainScaffold(navController, selectedRoute = Routes.Cart) {
                CartScreen(
                    onProceedToPayment = {
                        navController.navigate(Routes.Payment)
                    }
                )
            }
        }
        composable(Routes.Profile) {
            MainScaffold(navController, selectedRoute = Routes.Profile) {
                ProfileScreen()
            }
        }
        composable(Routes.Payment) {
            MainScaffold(navController, selectedRoute = Routes.Cart) {
                PaymentScreen(
                    onBackToCart = { navController.popBackStack() },
                    onOrderPlaced = {
                        navController.navigate(Routes.Home) {
                            popUpTo(Routes.Home) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun LaunchScreen(onFinished: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2000)
        onFinished()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2E7D32)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.quickmart_logo),
                    contentDescription = "QuickMart logo",
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                text = "QuickMart",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Shop faster, shop smarter",
                color = Color(0xFFF9A825)
            )
        }
    }
}

@Composable
fun MainScaffold(
    navController: NavHostController,
    selectedRoute: String,
    content: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomNavBar(navController = navController, selectedRoute = selectedRoute)
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            content()
        }
    }
}

@Composable
fun BottomNavBar(navController: NavHostController, selectedRoute: String) {
    val items = listOf(
        Routes.Cart to Icons.Filled.ShoppingCart,
        Routes.Home to Icons.Filled.Home,
        Routes.Catalog to Icons.Filled.List,
        Routes.Profile to Icons.Filled.AccountCircle
    )

    NavigationBar(
        containerColor = Color.White
    ) {
        items.forEach { (route, icon) ->
            NavigationBarItem(
                selected = selectedRoute == route,
                onClick = {
                    if (selectedRoute != route) {
                        navController.navigate(route) {
                            popUpTo(Routes.Home) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = route,
                        tint = if (selectedRoute == route) Color(0xFF2E7D32) else Color.Gray
                    )
                },
                label = {
                    Text(
                        text = when (route) {
                            Routes.Cart -> "Cart"
                            Routes.Home -> "Home"
                            Routes.Catalog -> "Catalog"
                            Routes.Profile -> "Profile"
                            else -> ""
                        },
                        fontSize = 10.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF2E7D32),
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = Color(0xFF2E7D32),
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}

data class Product(
    val id: Int,
    val name: String,
    val price: String,
    val imageRes: Int
)

data class CartItem(
    val product: Product,
    var quantity: Int
)

private val sampleProducts = listOf(
    Product(1, "Wireless Bluetooth Earbuds", "$8.99", R.drawable.earbuds),
    Product(2, "LED Galaxy Star Projector Lamp", "$12.49", R.drawable.star_projector),
    Product(3, "Mini Portable Handheld Fan USB", "$4.79", R.drawable.mini_fan),
    Product(4, "Trendy Oversized T‑Shirt (Unisex)", "$6.39", R.drawable.oversized_tshirt),
    Product(5, "Waterproof Smart Fitness Band", "$11.99", R.drawable.fitness_band),
    Product(6, "Silicone Cable Organizer Clips (6 pcs)", "$2.49", R.drawable.cable_clips),
    Product(7, "Cute Cartoon Phone Case", "$3.59", R.drawable.phone_case),
    Product(8, "RGB Gaming Mouse Pad Large", "$9.99", R.drawable.gaming_mousepad),
    Product(9, "Foldable Laptop Stand Metal", "$7.49", R.drawable.laptop_stand),
    Product(10, "Korean Style Shoulder Bag", "$10.29", R.drawable.shoulder_bag),
)

data class BannerInfo(
    val title: String,
    val subtitle: String,
    val imageRes: Int
)

private val bannerItems = listOf(
    BannerInfo(
        "Up to 80% OFF Flash Deals",
        "Limited time offers just for you",
        R.drawable.banner_flash_deals
    ),
    BannerInfo(
        "Hot Gadgets Under $10",
        "Earbuds, lamps, fans & more",
        R.drawable.banner_gadgets
    ),
    BannerInfo(
        "Trending Fashion Picks",
        "Oversized tees, bags & more",
        R.drawable.banner_fashion
    ),
    BannerInfo(
        "Free Shipping on Selected Items",
        "Look for the free shipping tag",
        R.drawable.banner_shipping
    )
)

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2E7D32)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Q",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.width(8.dp))
            Column {
                Text(
                    text = "QuickMart",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )
                Text(
                    text = "Welcome back!",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            items(bannerItems) { banner ->
                BannerCard(banner = banner)
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxSize()
                .padding(horizontal = 16.dp)
            ,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sampleProducts) { product ->
                ProductCard(product = product)
            }
            item { Spacer(Modifier.height(12.dp)) }
        }
    }
}

@Composable
fun CatalogScreen() {
    val categories = listOf(
        "All",
        "Flash Deals",
        "Fashion",
        "Gadgets",
        "Home & Living",
        "Beauty",
        "Accessories"
    )
    var selectedCategory by remember { mutableStateOf("All") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Catalog",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )
            Text(
                text = "QuickMart",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { cat ->
                CategoryChip(
                    text = cat,
                    selected = selectedCategory == cat,
                    onClick = { selectedCategory = cat }
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sampleProducts) { product ->
                CatalogProductRow(product = product)
            }
            item { Spacer(Modifier.height(12.dp)) }
        }
    }
}

@Composable
fun CategoryChip(text: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (selected) Color(0xFF2E7D32) else Color(0xFFF0F0F0))
            .clickableWithoutRipple(onClick)
            .padding(horizontal = 14.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            color = if (selected) Color.White else Color.Black,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
fun CatalogProductRow(product: Product) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFE8F5E9)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = product.imageRes),
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Trending item • Hot deal",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = product.price,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF2E7D32))
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Add",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun BannerCard(banner: BannerInfo) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .width(260.dp)
            .height(140.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = banner.imageRes),
                contentDescription = banner.title,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Text(
                    text = banner.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = banner.subtitle,
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun ProductCard(product: Product) {
    var isFavorite by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
        elevation = CardDefaults.cardElevation(3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product image
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFE8F5E9)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = product.imageRes),
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(Modifier.width(12.dp))

            // Name + price
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = product.price,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )
            }

            Spacer(Modifier.width(8.dp))

            // Favorite + Add button in a column
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) Color(0xFFF44336) else Color.Gray,
                    modifier = Modifier
                        .size(22.dp)
                        .clickableWithoutRipple { isFavorite = !isFavorite }
                )

                Spacer(Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF2E7D32))
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Add",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun CartScreen(onProceedToPayment: () -> Unit) {
    var cartItems by remember {
        mutableStateOf(
            sampleProducts.take(3).map { CartItem(it, quantity = 1) }
        )
    }

    val subtotal = cartItems.sumOf { item ->
        val value = item.product.price.removePrefix("$").toDoubleOrNull() ?: 0.0
        value * item.quantity
    }
    val delivery = 0.0
    val total = subtotal + delivery

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        StepIndicator(currentStep = 1)

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Your Cart",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2E7D32)
        )

        Spacer(Modifier.height(8.dp))

        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Your cart is empty", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(cartItems, key = { it.product.id }) { item ->
                    CartItemRow(
                        item = item,
                        onQuantityChange = { newQty ->
                            cartItems = cartItems.map {
                                if (it.product.id == item.product.id)
                                    it.copy(quantity = newQty.coerceAtLeast(1))
                                else it
                            }
                        },
                        onRemove = {
                            cartItems = cartItems.filter { it.product.id != item.product.id }
                        }
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FBE7)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    SummaryRow("Subtotal", "$${"%.2f".format(subtotal)}")
                    SummaryRow("Delivery (COD)", "Free")
                    Spacer(Modifier.height(8.dp))
                    SummaryRow("Total", "$${"%.2f".format(total)}", bold = true)
                }
            }

            Spacer(Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(32.dp))
                    .background(Color(0xFF2E7D32))
                    .clickableWithoutRipple { onProceedToPayment() }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Proceed to Payment (Cash on Delivery)",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun StepIndicator(currentStep: Int) {
    val steps = listOf("Cart", "Address", "Payment")
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        steps.forEachIndexed { index, label ->
            val stepNumber = index + 1
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(
                            if (stepNumber <= currentStep) Color(0xFF2E7D32) else Color(0xFFE0E0E0)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stepNumber.toString(),
                        color = if (stepNumber <= currentStep) Color.White else Color.Gray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text = label,
                    fontSize = 11.sp,
                    color = if (stepNumber <= currentStep) Color(0xFF2E7D32) else Color.Gray
                )
            }
        }
    }
}

@Composable
fun CartItemRow(
    item: CartItem,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFE8F5E9)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = item.product.imageRes),
                    contentDescription = item.product.name,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.product.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = item.product.price,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0E0E0))
                            .clickableWithoutRipple { onQuantityChange(item.quantity - 1) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("-", fontWeight = FontWeight.Bold)
                    }
                    Text(
                        text = item.quantity.toString(),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2E7D32))
                            .clickableWithoutRipple { onQuantityChange(item.quantity + 1) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("+", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "Remove",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    modifier = Modifier.clickableWithoutRipple { onRemove() }
                )
            }
        }
    }
}

@Composable
fun SummaryRow(label: String, value: String, bold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = if (bold) FontWeight.SemiBold else FontWeight.Normal
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = if (bold) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
fun PaymentScreen(
    onBackToCart: () -> Unit,
    onOrderPlaced: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        StepIndicator(currentStep = 3)

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Payment",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2E7D32)
        )

        Spacer(Modifier.height(12.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Cash on Delivery",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "You will pay in cash when the order is delivered to your address.",
                    fontSize = 13.sp
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Delivery Address",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Add your default address UI here (static for now).",
            fontSize = 13.sp,
            color = Color.Gray
        )

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(32.dp))
                    .background(Color(0xFFE0E0E0))
                    .clickableWithoutRipple { onBackToCart() }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Back to Cart", color = Color.Black)
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(32.dp))
                    .background(Color(0xFF2E7D32))
                    .clickableWithoutRipple { onOrderPlaced() }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Place Order (COD)",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ProfileScreen() {
    val userName = "John Doe"
    val email = "john.doe@gmail.com"
    val phone = "+94 71 234 5678"
    val address = "No. 12, Lake Road,\nColombo, Sri Lanka"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Profile",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )
            Text(
                text = "QuickMart",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        Spacer(Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2E7D32)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = userName.take(2).uppercase(),
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    text = userName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = email,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ProfileStatCard(title = "Orders", value = "12")
            ProfileStatCard(title = "In Cart", value = "3")
            ProfileStatCard(title = "Favourites", value = "8")
        }

        Spacer(Modifier.height(20.dp))

        Text(
            text = "Delivery Address",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(6.dp))
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = userName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(4.dp))
                Text(text = phone, fontSize = 13.sp)
                Spacer(Modifier.height(4.dp))
                Text(text = address, fontSize = 13.sp)
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Change address",
                    fontSize = 12.sp,
                    color = Color(0xFFF9A825),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickableWithoutRipple { }
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        Text(
            text = "Account",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(8.dp))

        ProfileActionRow("My Orders")
        ProfileActionRow("Payment Methods (COD only)")
        ProfileActionRow("Notifications")
        ProfileActionRow("Help & Support")

        Spacer(Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(32.dp))
                .background(Color(0xFFE53935))
                .clickableWithoutRipple { }
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Logout",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ProfileStatCard(title: String, value: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FBE7)),
        modifier = Modifier
            .padding(horizontal = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )
            Text(
                text = title,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun ProfileActionRow(label: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF5F5F5))
            .clickableWithoutRipple { }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = ">",
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
    Spacer(Modifier.height(8.dp))
}

@Composable
fun SimpleCenterScreen(title: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2E7D32)
        )
    }
}

private fun Modifier.clickableWithoutRipple(onClick: () -> Unit): Modifier =
    this.then(
        clickable(
            indication = null,
            interactionSource = MutableInteractionSource(),
            onClick = onClick
        )
    )