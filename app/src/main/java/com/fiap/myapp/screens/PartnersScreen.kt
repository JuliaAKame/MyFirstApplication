package com.fiap.myapp.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.fiap.myapp.R
import com.fiap.myapp.ui.theme.BLUE
import com.fiap.myapp.ui.theme.GREEN
import com.fiap.myapp.ui.theme.WHITE

private val Righteous = FontFamily(Font(R.font.righteous_regular))

private val LOGO_SIZE = 64.dp
private val LOGO_PADDING = 8.dp
private val ITEM_PADDING = 16.dp
private val CARD_CORNER = 12.dp
private val BUTTON_HEIGHT = 48.dp

private data class Partner(val id: String, val name: String, val benefit: String, val logoRes: Int?)

private val PARTNERS = listOf(
	Partner("amazon", "Amazon", "Troque pontos por cupons de até 20% na Amazon.", R.drawable.amazon_logo),
	Partner("mercado_livre", "Mercado Livre", "Pontos que geram descontos e frete grátis.", R.drawable.mercadolivre_logo),
	Partner("ifood", "Ifood", "Descontos de até 10% em pedidos parceiros.", R.drawable.ifood_logo),
	Partner("fiap", "FIAP", "Pontos trocáveis por cursos e descontos educativos.", R.drawable.fiap_logo),
	Partner("uber", "Uber", "Ganhe créditos de viagem em campanhas de reciclagem.", R.drawable.uber_logo)
)

private val PARTNER_OFFERS = mapOf(
	"amazon" to "produtos selecionados na Amazon",
	"mercado_livre" to "compras sustentáveis no Mercado Livre",
	"ifood" to "pedidos parceiros no iFood",
	"fiap" to "cursos e workshops na FIAP",
	"uber" to "viagens com Uber"
)

@Composable
private fun PartnerItem(partner: Partner, onAction: (String) -> Unit, onResgatar: (Partner) -> Unit) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 8.dp),
		shape = RoundedCornerShape(CARD_CORNER),
		colors = CardDefaults.cardColors(containerColor = WHITE.copy(alpha = 0.06f)),
		elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(ITEM_PADDING),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(12.dp)
		) {
			if (partner.logoRes != null) {
				Box(
					modifier = Modifier
						.size(LOGO_SIZE)
						.clip(RoundedCornerShape(CARD_CORNER))
						.background(color = WHITE.copy(alpha = 0.06f)),
					contentAlignment = Alignment.Center
				) {
					Image(
						painter = painterResource(id = partner.logoRes),
						contentDescription = partner.name,
						modifier = Modifier
							.padding(LOGO_PADDING)
							.fillMaxSize(),
						contentScale = ContentScale.Fit
					)
				}
			} else {
				Box(
					modifier = Modifier
						.size(LOGO_SIZE)
						.clip(RoundedCornerShape(CARD_CORNER))
						.background(color = WHITE.copy(alpha = 0.06f)),
					contentAlignment = Alignment.Center
				) {
					Text(text = partner.name.take(1), color = WHITE, fontWeight = FontWeight.Bold)
				}
			}

			Column(modifier = Modifier.weight(1f)) {
				Text(text = partner.name, color = WHITE, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
				Spacer(modifier = Modifier.height(6.dp))
				Text(text = partner.benefit, color = WHITE, fontSize = 14.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
			}

			Button(
				onClick = { onResgatar(partner) },
				colors = ButtonDefaults.buttonColors(containerColor = BLUE),
				modifier = Modifier
					.height(BUTTON_HEIGHT)
					.shadow(8.dp, shape = RoundedCornerShape(8.dp))
			) {
				Text(text = "Resgatar", color = WHITE, fontWeight = FontWeight.Bold)
			}
		}
	}
}

@Composable
fun PartnersScreen(onBack: () -> Unit = {}, onAction: (String) -> Unit = {}, onLogout: () -> Unit = {}) {
	var selectedPartner by remember { mutableStateOf<Partner?>(null) }
	var showLogoutDialog by remember { mutableStateOf(false) }

	val clipboardManager = LocalClipboardManager.current

	LazyColumn(
		modifier = Modifier
			.fillMaxSize()
			.background(
				brush = Brush.linearGradient(colors = listOf(GREEN, BLUE))
			),
		contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
	) {
		item {
			// Header com botão de logout
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.statusBarsPadding()
					.padding(horizontal = 4.dp, vertical = 8.dp),
				horizontalArrangement = Arrangement.SpaceBetween,
				verticalAlignment = Alignment.CenterVertically
			) {
				Spacer(modifier = Modifier.size(40.dp)) // Espaço para equilibrar o layout
				
				Text(
					text = buildAnnotatedString {
						append("CLEAN")
						withStyle(style = SpanStyle(color = BLUE, fontFamily = Righteous)) { 
							append("WORLD") 
						}
					},
					color = WHITE,
					fontSize = 32.sp,
					fontWeight = FontWeight.Bold,
					textAlign = TextAlign.Center
				)
				
				IconButton(
					onClick = { showLogoutDialog = true },
					modifier = Modifier.size(40.dp)
				) {
					Icon(
						imageVector = androidx.compose.material.icons.Icons.Default.ExitToApp,
						contentDescription = "Sair",
						tint = WHITE,
						modifier = Modifier.size(24.dp)
					)
				}
			}

			Text(
				text = "Confira nossos parceiros e seus benefícios",
				color = WHITE,
				fontSize = 18.sp,
				fontWeight = FontWeight.SemiBold,
				textAlign = TextAlign.Center,
				modifier = Modifier
					.fillMaxWidth()
					.padding(bottom = 16.dp)
			)
		}

		items(PARTNERS) { partner ->
			PartnerItem(partner = partner, onAction = onAction, onResgatar = { p -> selectedPartner = p })
		}

		item {
			// Footer - removido o botão "Voltar" pois agora temos logout no header
			Spacer(modifier = Modifier.height(20.dp))

			// Dialog de cupom
			selectedPartner?.let { partner ->
				val coupon = "${partner.name.replace(" ", "").uppercase()}10CLEANW"
				val offerText = PARTNER_OFFERS[partner.id] ?: partner.benefit
				AlertDialog(
					onDismissRequest = { selectedPartner = null },
					title = { Text(text = "Cupom disponível") },
					text = { Text(text = "Utilize o CUPOM $coupon para obter 10% de desconto em $offerText") },
					confirmButton = {
						TextButton(onClick = {
							clipboardManager.setText(AnnotatedString(coupon))
							selectedPartner = null
						}) { Text("Copiar") }
					},
					dismissButton = {
						TextButton(onClick = { selectedPartner = null }) { Text("Fechar") }
					}
				)
			}
			
			// Dialog de logout
			if (showLogoutDialog) {
				AlertDialog(
					onDismissRequest = { showLogoutDialog = false },
					title = { 
						Text(
							text = "Sair da conta",
							fontWeight = FontWeight.Bold
						) 
					},
					text = { 
						Text("Tem certeza de que deseja sair da sua conta?") 
					},
					confirmButton = {
						TextButton(
							onClick = {
								showLogoutDialog = false
								onLogout()
							}
						) {
							Text("Sair", color = MaterialTheme.colorScheme.error)
						}
					},
					dismissButton = {
						TextButton(onClick = { showLogoutDialog = false }) {
							Text("Cancelar")
						}
					}
				)
			}
		}
	}
}

@Preview
@Composable
private fun PartnersScreenPreview() {
	PartnersScreen()
}


