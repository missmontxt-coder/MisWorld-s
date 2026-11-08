package com.misworlds.app

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Ink = Color(0xFF17132F)
private val Purple = Color(0xFF9B6BFF)
private val Pink = Color(0xFFFF5FA2)
private val Cyan = Color(0xFF42D9E8)
private val Gold = Color(0xFFFFC857)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences("misworlds_local", Context.MODE_PRIVATE)
        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Purple, secondary = Pink, background = Ink,
                    surface = Color(0xFF292348), onPrimary = Color.White,
                    onBackground = Color.White, onSurface = Color.White
                )
            ) {
                MisWorldsApp(
                    saved = prefs.getBoolean("onboarding_done", false),
                    savedName = prefs.getString("display_name", "") ?: "",
                    savedDtfCoins = prefs.getInt("dtf_coins", 120),
                    savedDtfXp = prefs.getInt("dtf_xp", 0),
                    savedDtfUnlocked = prefs.getStringSet("dtf_unlocked", emptySet())?.toSet() ?: emptySet(),
                    onSaveDtfProgress = { coins, xp, unlocked ->
                        prefs.edit()
                            .putInt("dtf_coins", coins)
                            .putInt("dtf_xp", xp)
                            .putStringSet("dtf_unlocked", unlocked)
                            .apply()
                    },
                    onSave = { name, age, gender, avatar ->
                        prefs.edit()
                            .putBoolean("onboarding_done", true)
                            .putString("display_name", name)
                            .putString("age_range", age)
                            .putString("gender", gender)
                            .putString("avatar", avatar)
                            .apply()
                    },
                    onReset = { prefs.edit().clear().apply() }
                )
            }
        }
    }
}

private data class World(
    val title: String, val subtitle: String, val icon: ImageVector, val color: Color
)

private val worlds = listOf(
    World("Creaciones", "Dibuja y crea", Icons.Filled.Palette, Color(0xFFE64E9B)),
    World("Mundos", "Convierte ideas", Icons.Filled.Lightbulb, Color(0xFFF59E0B)),
    World("Mi aventura", "Explora experiencias", Icons.Filled.Explore, Color(0xFF22B8A7)),
    World("Mi business", "Organiza tu negocio", Icons.Filled.Storefront, Color(0xFF3987EF)),
    World("Mi colección", "Guarda tus tesoros", Icons.Filled.Collections, Color(0xFFA16BDE)),
    World("Mi música", "Crea canciones", Icons.Filled.MusicNote, Color(0xFFE34E75)),
    World("Mi estilo", "Expresa quién eres", Icons.Filled.Checkroom, Color(0xFFE58C39)),
    World("Mi Marca", "Construye tu marca", Icons.Filled.Stars, Color(0xFFD5A52A)),
    World("Mi productividad", "Alcanza tus metas", Icons.Filled.TrackChanges, Color(0xFF35B98B))
)

@Composable
private fun MisWorldsApp(
    saved: Boolean,
    savedName: String,
    onSave: (String, String, String, String) -> Unit,
    onReset: () -> Unit,
    savedDtfCoins: Int,
    savedDtfXp: Int,
    savedDtfUnlocked: Set<String>,
    onSaveDtfProgress: (Int, Int, Set<String>) -> Unit
) {
    var screen by remember { mutableStateOf(if (saved) "home" else "splash") }
    var age by remember { mutableStateOf("18–24") }
    var name by remember { mutableStateOf(savedName) }
    var gender by remember { mutableStateOf("Oculto") }
    var avatar by remember { mutableStateOf("Burbuja") }
    var selectedWorld by remember { mutableStateOf<World?>(null) }
    var dtfCoins by remember { mutableStateOf(savedDtfCoins) }
    var dtfXp by remember { mutableStateOf(savedDtfXp) }
    var dtfUnlocked by remember { mutableStateOf(savedDtfUnlocked) }

    fun saveDtf(
        coins: Int = dtfCoins,
        xp: Int = dtfXp,
        unlocked: Set<String> = dtfUnlocked
    ) {
        dtfCoins = coins
        dtfXp = xp
        dtfUnlocked = unlocked
        onSaveDtfProgress(coins, xp, unlocked)
    }

    Box(
        modifier = Modifier.fillMaxSize().background(
            Brush.linearGradient(listOf(Ink, Color(0xFF30205C), Color(0xFF102C4A)))
        )
    ) {
        when (screen) {
            "splash" -> Column(
                Modifier.fillMaxSize().padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("✦", color = Gold, fontSize = 54.sp)
                Text("MisWorld", fontSize = 42.sp, fontWeight = FontWeight.Black,
                    color = Color.White)
                Text("TU UNIVERSO COMIENZA AQUÍ", color = Color(0xFFD8C5FF),
                    fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                Spacer(Modifier.height(26.dp))
                Text("Un lugar donde tus ideas se convierten en mundos.",
                    color = Color.White.copy(alpha = .85f), textAlign = TextAlign.Center)
                Spacer(Modifier.height(34.dp))
                MWButton("COMENZAR") { screen = "age" }
            }

            "age" -> OnboardingFrame(
                "¿Qué edad tienes?",
                "MisWorld's es un universo para todas las edades."
            ) {
                Text(
                    "Selecciona tu rango de edad:",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                listOf("Menos de 13", "13–17", "18–24", "25–34", "35–44", "45 o más").forEach {
                    SelectRow(it, age == it) { age = it }
                }

                Text(
                    "Algunas funciones pueden tener restricciones según la edad.",
                    color = Gold,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )

                MWButton("CONTINUAR") {
                    screen = "name"
                }
            }

            "name" -> OnboardingFrame("¿Cómo te llamamos?", "Puedes usar un apodo.") {
                OutlinedTextField(
                    value = name, onValueChange = { name = it.take(30) },
                    label = { Text("Nombre o apodo") }, singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = fieldColors()
                )
                MWButton("CONTINUAR", enabled = name.isNotBlank()) { screen = "welcome" }
            }

            "welcome" -> OnboardingFrame("¡Bienvenido a MisWorld!", "Hola, ${name.ifBlank { "explorador" }}. Aquí comienza tu historia.") {
                Text("Explora, crea y construye tu propio universo.",
                    color = Color.White, textAlign = TextAlign.Center)
                Spacer(Modifier.height(20.dp))
                Text("By MisaelAlanGonzalezMontecinos",
                    color = Color(0xFFD8B8FF), fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center)
                MWButton("CONTINUAR") { screen = "gender" }
            }

            "gender" -> OnboardingFrame("Selecciona tu sexo", "Esta opción es voluntaria.") {
                listOf("Hombre", "Mujer", "Oculto").forEach {
                    SelectRow(it, gender == it) { gender = it }
                }
                MWButton("CONTINUAR") { screen = "avatar" }
            }

            "avatar" -> OnboardingFrame("Elige tu primer avatar", "Podrás personalizarlo más adelante.") {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    AvatarChoice("Burbuja", "●", avatar == "Burbuja", Modifier.weight(1f)) { avatar = "Burbuja" }
                    AvatarChoice("Silueta", "◉", avatar == "Silueta", Modifier.weight(1f)) { avatar = "Silueta" }
                }
                MWButton("CONTINUAR") { screen = "account" }
            }

            "account" -> OnboardingFrame("Tu acceso a MisWorlds", "Primera versión: perfil local de prueba.") {
                Text("Todavía no hay registro en línea. No escribas ni compartas contraseñas en esta versión.",
                    color = Color(0xFFFFD58A), textAlign = TextAlign.Center)
                MWButton("CONTINUAR EN MODO LOCAL") {
                    onSave(name.trim(), age, gender, avatar)
                    screen = "home"
                }
            }

            "home" -> {
                Column(Modifier.fillMaxSize().padding(horizontal = 18.dp, vertical = 20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text("MisWorlds", fontSize = 30.sp, fontWeight = FontWeight.Black,
                                color = Color.White)
                            Text("Hola, ${name.ifBlank { savedName.ifBlank { "explorador" } }} ✨",
                                color = Color(0xFFD8C5FF))
                        }
                        Text(if (avatar == "Burbuja") "●" else "◉",
                            color = Cyan, fontSize = 34.sp)
                    }
                    Spacer(Modifier.height(18.dp))
                    Text("¿Qué mundo quieres explorar hoy?",
                        color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(14.dp))
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(bottom = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(worlds) { world ->
                            WorldCard(world) {
                                selectedWorld = world
                                screen = "world"
                            }
                        }
                        item {
                            CollectionCard("PERSONAJES", "🎨", "Diseños para impresión") {
                                screen = "dtf"
                            }
                        }
                        item {
                            CollectionCard("EQUIPAMIENTOS", "🖼️", "Imágenes digitales") {
                                screen = "png"
                            }
                        }
                    }
                }
            }

            "dtf" -> {
                DtfCatalogScreen(
                    coins = dtfCoins,
                    xp = dtfXp,
                    unlocked = dtfUnlocked,
                    onEarnXp = { saveDtf(xp = dtfXp + 50) },
                    onUnlock = { design ->
                        if (design.id !in dtfUnlocked &&
                            !design.eventOnly &&
                            design.secretCode.isBlank() &&
                            dtfCoins >= design.coins &&
                            dtfXp >= design.requiredXp
                        ) {
                            saveDtf(
                                coins = dtfCoins - design.coins,
                                unlocked = dtfUnlocked + design.id
                            )
                        }
                    },
                    onRedeemCode = { code ->
                        val legendary = dtfDesigns.first { it.id == "legendary" }
                        if (code.trim().uppercase() == legendary.secretCode &&
                            legendary.id !in dtfUnlocked
                        ) {
                            saveDtf(unlocked = dtfUnlocked + legendary.id)
                            true
                        } else {
                            false
                        }
                    },
                    onOpenGallery = { screen = "dtf_gallery" },
                    onBack = { screen = "home" }
                )
            }

            "dtf_gallery" -> DtfGalleryScreen(
                onBack = { screen = "dtf" }
            )

            "png" -> OnboardingFrame(
                "🖼️ EQUIPAMIENTOS",
                "Tu colección de equipamientos digitales."
            ) {
                Text(
                    "Próximamente encontrarás equipamientos digitales.",
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                MWButton("VOLVER AL MENÚ") { screen = "home" }
            }

            "world" -> {
                val world = selectedWorld
                if (world != null && world.title in listOf(
                        "Creaciones", "Mi colección", "Mi música"
                    )
                ) {
                    WorldFilesScreen(world = world) { screen = "home" }
                } else {
                    Column(
                        Modifier.fillMaxSize().padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (world != null) {
                            Icon(world.icon, null, tint = world.color, modifier = Modifier.size(72.dp))
                            Spacer(Modifier.height(16.dp))
                            Text(world.title, color = Color.White, fontSize = 30.sp,
                                fontWeight = FontWeight.Bold)
                            Text(world.subtitle, color = Color(0xFFD8C5FF))
                            Spacer(Modifier.height(20.dp))
                            Text("Este espacio se desarrollará en la siguiente etapa.",
                                color = Color.White, textAlign = TextAlign.Center)
                        }
                        Spacer(Modifier.height(28.dp))
                        MWButton("VOLVER A MIS MUNDOS") { screen = "home" }
                    }
                }
            }
        }
    }
}

private data class DtfDesign(
    val id: String,
    val name: String,
    val symbol: String,
    val rarity: String,
    val description: String,
    val coins: Int = 0,
    val requiredXp: Int = 0,
    val secretCode: String = "",
    val eventOnly: Boolean = false
)

private val dtfDesigns = listOf(
    DtfDesign("nebula", "Nebulosa", "✦", "COMÚN", "Un misterio cósmico.", coins = 30),
    DtfDesign("planet", "Planeta oculto", "◉", "COMÚN", "Un mundo por descubrir.", coins = 40),
    DtfDesign("guardian", "Guardián estelar", "♢", "RARO", "Una criatura dimensional.", requiredXp = 100),
    DtfDesign("dragon", "Dragón cósmico", "♜", "ÉPICO", "Una fuerza galáctica.", coins = 60, requiredXp = 150),
    DtfDesign("legendary", "Portal legendario", "⌘", "LEGENDARIO", "Un secreto del multiverso.", secretCode = "MISWORLDS"),
    DtfDesign("mythic", "Fénix del multiverso", "✧", "MÍTICO", "Disponible en eventos.", eventOnly = true)
)

@Composable
private fun DtfCatalogScreen(
    coins: Int,
    xp: Int,
    unlocked: Set<String>,
    onEarnXp: () -> Unit,
    onUnlock: (DtfDesign) -> Unit,
    onRedeemCode: (String) -> Boolean,
    onOpenGallery: () -> Unit,
    onBack: () -> Unit
) {
    var code by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        Modifier.fillMaxSize().padding(18.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            MWBackButton(onBack = onBack)
        }

        Text("🎨 PERSONAJES", color = Color.White, fontSize = 27.sp, fontWeight = FontWeight.Black)
        Text("Descubre diseños ocultos del multiverso.", color = Color(0xFFD8C5FF))
        Spacer(Modifier.height(16.dp))

        Text("🪙 $coins monedas     ⭐ $xp XP     ${unlocked.size}/6 diseños",
            color = Gold, fontWeight = FontWeight.Bold)

        MWButton("GANAR 50 XP · DEMOSTRACIÓN", onClick = onEarnXp)
        MWButton("VER LOS 48 PERSONAJES", onClick = onOpenGallery)

        dtfDesigns.forEach { design ->
            val isUnlocked = design.id in unlocked
            val canUnlock = !isUnlocked && !design.eventOnly &&
                design.secretCode.isBlank() && coins >= design.coins &&
                xp >= design.requiredXp

            Column(
                Modifier.fillMaxWidth().padding(vertical = 6.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFF292348)).padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        if (isUnlocked) design.symbol else "◈",
                        color = if (isUnlocked) Gold else Color(0xFF45405B),
                        fontSize = 42.sp
                    )
                    Column(Modifier.weight(1f).padding(start = 14.dp)) {
                        Text(if (isUnlocked) design.name else "Diseño oculto",
                            color = Color.White, fontWeight = FontWeight.Bold)
                        Text(design.rarity, color = Gold, fontSize = 12.sp)
                        Text(if (isUnlocked) "Desbloqueado" else design.description,
                            color = Color(0xFFD8C5FF), fontSize = 12.sp)
                    }
                }

                when {
                    isUnlocked -> Text("✓ EN TU COLECCIÓN", color = Cyan)
                    design.eventOnly -> Text("🔒 Evento especial futuro", color = Pink)
                    design.secretCode.isNotBlank() -> Text("🔐 Requiere código secreto", color = Pink)
                    else -> {
                        Text("${design.coins} monedas · ${design.requiredXp} XP",
                            color = Color.White, fontSize = 12.sp)
                        MWButton("DESBLOQUEAR", enabled = canUnlock) {
                            onUnlock(design)
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        Text("CÓDIGO SECRETO", color = Gold, fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = code,
            onValueChange = { code = it.take(30) },
            label = { Text("Introduce tu código") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = fieldColors()
        )
        MWButton("CANJEAR CÓDIGO", enabled = code.isNotBlank()) {
            message = if (onRedeemCode(code)) {
                code = ""
                "¡Portal legendario desbloqueado!"
            } else "Código incorrecto o ya utilizado."
        }
        if (message.isNotBlank()) Text(message, color = Cyan)

        Spacer(Modifier.height(12.dp))
        MWButton("VOLVER AL MENÚ", onClick = onBack)
    }
}


@Composable
private fun DtfGalleryScreen(onBack: () -> Unit) {
    Column(
        Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            MWBackButton(onBack = onBack)
        }

        Text(
            "🌌 GALERÍA DTF",
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Black
        )
        Text(
            "48 diseños originales · NEON FIRST EDITION",
            color = Color(0xFFD8C5FF)
        )
        Spacer(Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items((1..48).toList()) { number ->
                DtfPreviewCard(number)
            }
        }

        MWButton("VOLVER A COLECCIÓN DTF", onClick = onBack)
    }
}

@Composable
private fun DtfPreviewCard(number: Int) {
    val context = LocalContext.current
    val fileName = "dtf_preview/${number.toString().padStart(3, '0')}.png"
    val bitmap = remember(number) {
        context.assets.open(fileName).use {
            BitmapFactory.decodeStream(it).asImageBitmap()
        }
    }

    Column(
        Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF292348))
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            bitmap = bitmap,
            contentDescription = "Diseño DTF ${number.toString().padStart(3, '0')}",
            modifier = Modifier.fillMaxWidth().height(150.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(Modifier.height(6.dp))
        Text(
            "DISEÑO ${number.toString().padStart(3, '0')}/048",
            color = Gold,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun OnboardingFrame(title: String, subtitle: String, content: @Composable ColumnScope.() -> Unit) {
    Column(
        Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(title, color = Color.White, fontSize = 27.sp, fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center)
        Spacer(Modifier.height(8.dp))
        Text(subtitle, color = Color(0xFFD8C5FF), textAlign = TextAlign.Center)
        Spacer(Modifier.height(24.dp))
        content()
    }
}

@Composable
private fun MWButton(label: String, enabled: Boolean = true, onClick: () -> Unit) {
    Button(
        onClick = onClick, enabled = enabled,
        modifier = Modifier.fillMaxWidth().padding(top = 12.dp).height(52.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Purple)
    ) { Text(label, fontWeight = FontWeight.Bold) }
}

@Composable
private fun SelectRow(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 4.dp).clip(RoundedCornerShape(14.dp))
            .background(if (selected) Color(0xFF59418A) else Color(0xFF292348))
            .clickable { onClick() }.padding(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected, onClick = onClick)
        Text(label, color = Color.White, modifier = Modifier.padding(start = 8.dp))
    }
}

@Composable
private fun AvatarChoice(label: String, symbol: String, selected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier.clip(RoundedCornerShape(20.dp))
            .background(if (selected) Color(0xFF59418A) else Color(0xFF292348))
            .clickable { onClick() }.padding(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(symbol, color = Cyan, fontSize = 56.sp)
        Text(label, color = Color.White, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun WorldCard(world: World, onClick: () -> Unit) {
    val shape = RoundedCornerShape(22.dp)

    Column(
        Modifier.fillMaxWidth()
            .height(158.dp)
            .clip(shape)
            .background(
                Brush.linearGradient(
                    listOf(
                        world.color.copy(alpha = 0.38f),
                        Color(0xFF292348),
                        Color(0xFF17132F)
                    )
                ),
                shape
            )
            .clickable { onClick() }
            .padding(15.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            world.icon,
            contentDescription = null,
            tint = world.color,
            modifier = Modifier.size(42.dp)
        )
        Column {
            Text(
                world.title,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp
            )
            Text(
                world.subtitle,
                color = Color(0xFFD8C5FF),
                fontSize = 11.sp
            )
            Text(
                "EXPLORAR  ↗",
                color = world.color,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun CollectionCard(
    title: String,
    symbol: String,
    subtitle: String,
    onClick: () -> Unit
) {
    val accent = when (title) {
        "PERSONAJES" -> Pink
        "EQUIPAMIENTOS" -> Cyan
        else -> Purple
    }

    val shape = RoundedCornerShape(22.dp)

    Column(
        Modifier.fillMaxWidth()
            .height(158.dp)
            .clip(shape)
            .background(
                Brush.linearGradient(
                    listOf(
                        accent.copy(alpha = 0.42f),
                        Color(0xFF292348),
                        Color(0xFF17132F)
                    )
                ),
                shape
            )
            .clickable { onClick() }
            .padding(15.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(symbol, fontSize = 40.sp)
        Column {
            Text(
                title,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp
            )
            Text(
                subtitle,
                color = Color(0xFFD8C5FF),
                fontSize = 11.sp
            )
            Text(
                "DESCUBRIR  ↗",
                color = accent,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White, unfocusedTextColor = Color.White,
    focusedBorderColor = Purple, unfocusedBorderColor = Color(0xFF8A78B8),
    focusedLabelColor = Color(0xFFD8B8FF), unfocusedLabelColor = Color(0xFFD8C5FF),
    cursorColor = Pink
)

@Composable
private fun MWBackButton(onBack: () -> Unit) {
    IconButton(onClick = onBack) {
        Icon(
            Icons.Filled.ArrowBack,
            contentDescription = "Regresar",
            tint = Cyan,
            modifier = Modifier.size(30.dp)
        )
    }
}




@Composable
private fun WorldFilesScreen(
    world: World,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val prefs = remember {
        context.getSharedPreferences("misworlds_local", Context.MODE_PRIVATE)
    }

    val storageKey = when (world.title) {
        "Creaciones" -> "files_creaciones"
        "Mi colección" -> "files_coleccion"
        else -> "files_musica"
    }

    val selectedFiles = remember(world.title) {
        mutableStateListOf<String>().apply {
            addAll(prefs.getStringSet(storageKey, emptySet())?.toList().orEmpty())
        }
    }

    val playerState = remember { mutableStateOf<MediaPlayer?>(null) }
    var activeFile by remember { mutableStateOf<String?>(null) }
    var isPlaying by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose {
            playerState.value?.release()
            playerState.value = null
        }
    }

    val mimeTypes = when (world.title) {
        "Creaciones" -> arrayOf("image/*")
        "Mi música" -> arrayOf("audio/*")
        else -> arrayOf(
            "image/*",
            "application/pdf",
            "text/*",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        )
    }

    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        uris.forEach { uri ->
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: SecurityException) {
            }

            val value = uri.toString()
            if (value !in selectedFiles) {
                selectedFiles.add(value)
            }
        }

        prefs.edit()
            .putStringSet(storageKey, selectedFiles.toSet())
            .apply()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            world.icon,
            contentDescription = null,
            tint = world.color,
            modifier = Modifier.size(64.dp)
        )

        Spacer(Modifier.height(12.dp))

        Text(
            world.title,
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            when (world.title) {
                "Creaciones" -> "Carga tus imágenes desde tu dispositivo."
                "Mi colección" -> "Guarda tus fotos, imágenes y documentos."
                else -> "Agrega tus archivos de música y audio."
            },
            color = Color(0xFFD8C5FF),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(24.dp))

        MWButton(
            when (world.title) {
                "Creaciones" -> "＋ CARGAR IMÁGENES"
                "Mi colección" -> "＋ CARGAR FOTO O DOCUMENTO"
                else -> "＋ SUBIR AUDIO"
            }
        ) {
            picker.launch(mimeTypes)
        }

        Spacer(Modifier.height(20.dp))

        Text(
            "ARCHIVOS GUARDADOS (${selectedFiles.size})",
            color = Cyan,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        if (selectedFiles.isEmpty()) {
            Text(
                "Todavía no has agregado archivos.",
                color = Color.LightGray,
                textAlign = TextAlign.Center
            )
        } else {
            selectedFiles.toList().forEach { file ->
                val uri = Uri.parse(file)
                val fileName = remember(file) {
                    var name = "Archivo"
                    context.contentResolver.query(
                        uri,
                        arrayOf(OpenableColumns.DISPLAY_NAME),
                        null,
                        null,
                        null
                    )?.use { cursor ->
                        if (cursor.moveToFirst()) {
                            val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                            if (index >= 0) name = cursor.getString(index) ?: name
                        }
                    }
                    name
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable(enabled = world.title == "Mi colección") {
                            val mimeType = context.contentResolver.getType(uri) ?: "*/*"
                            val openIntent = Intent(Intent.ACTION_VIEW)
                                .setDataAndType(uri, mimeType)
                                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            runCatching {
                                context.startActivity(
                                    Intent.createChooser(openIntent, "Abrir archivo")
                                )
                            }
                        },
                    color = Color(0xFF292348),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (world.title == "Creaciones") {
                            val previewBitmap = remember(file) {
                                context.contentResolver.openInputStream(uri)?.use {
                                    BitmapFactory.decodeStream(it)?.asImageBitmap()
                                }
                            }
                            if (previewBitmap != null) {
                                Image(
                                    bitmap = previewBitmap,
                                    contentDescription = fileName,
                                    modifier = Modifier.size(64.dp),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Icon(Icons.Filled.Image, null, tint = world.color)
                            }
                        } else {
                            Icon(
                                if (world.title == "Mi música")
                                    Icons.Filled.MusicNote
                                else Icons.Filled.InsertDriveFile,
                                contentDescription = null,
                                tint = world.color
                            )
                        }

                        Spacer(Modifier.width(10.dp))

                        Text(
                            fileName,
                            color = Color.White,
                            modifier = Modifier.weight(1f),
                            maxLines = 2
                        )

                        if (world.title == "Mi música") {
                            IconButton(onClick = {
                                val current = playerState.value

                                if (activeFile == file && current != null) {
                                    if (isPlaying) {
                                        current.pause()
                                        isPlaying = false
                                    } else {
                                        current.start()
                                        isPlaying = true
                                    }
                                } else {
                                    playerState.value?.release()
                                    playerState.value = null
                                    activeFile = null
                                    isPlaying = false

                                    val newPlayer = runCatching {
                                        MediaPlayer.create(context, uri)
                                    }.getOrNull()

                                    if (newPlayer != null) {
                                        playerState.value = newPlayer
                                        activeFile = file
                                        newPlayer.setOnCompletionListener {
                                            it.release()
                                            playerState.value = null
                                            activeFile = null
                                            isPlaying = false
                                        }
                                        newPlayer.start()
                                        isPlaying = true
                                    }
                                }
                            }) {
                                Icon(
                                    if (activeFile == file && isPlaying)
                                        Icons.Filled.Pause
                                    else Icons.Filled.PlayArrow,
                                    contentDescription = "Reproducir o pausar",
                                    tint = Cyan
                                )
                            }
                        }

                        IconButton(onClick = {
                            selectedFiles.remove(file)
                            prefs.edit()
                                .putStringSet(storageKey, selectedFiles.toSet())
                                .apply()
                        }) {
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = "Eliminar archivo",
                                tint = Pink
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(28.dp))
        MWButton("VOLVER A MIS MUNDOS", onClick = onBack)
    }
}
