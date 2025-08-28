package com.fiap.myapp.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fiap.myapp.R
import com.fiap.myapp.ui.theme.BLUE
import com.fiap.myapp.ui.theme.GREEN
import com.fiap.myapp.ui.theme.WHITE

val MinhaFontePartners = FontFamily(
	Font(R.font.righteous_regular)
)

@Composable
fun PartnersScreen(onBack: () -> Unit = {}, onAction: (String) -> Unit = {}) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(
				brush = Brush.linearGradient(
					colors = listOf(
						GREEN,
						BLUE
					)
				)
			)
			.verticalScroll(rememberScrollState()),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Top
	) {
		Text(
			text = buildAnnotatedString {
				append("CLEAN")
				withStyle(
					style = SpanStyle(
						color = BLUE,
						fontFamily = MinhaFontePartners
					)
				) {
					append("WORLD\n")
				}
			},
			color = WHITE,
			fontSize = 40.sp,
			fontWeight = FontWeight.Bold,
			textAlign = TextAlign.Center,
			modifier = Modifier
				.fillMaxWidth()
				.padding(top = 16.dp, bottom = 8.dp)
		)

		Text(
			text = "Confira nossos parceiros e seus benefícios",
			color = WHITE,
			fontSize = 18.sp,
			fontWeight = FontWeight.SemiBold,
			textAlign = TextAlign.Center,
			modifier = Modifier
				.fillMaxWidth()
				.padding(20.dp, 20.dp, 20.dp, 8.dp)
		)

		Column(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 16.dp, vertical = 8.dp)
		) {
			data class Partner(val id: String, val name: String, val benefit: String, val logoRes: Int? = null)

			val partners = listOf(
				Partner(
					"amazon",
					"Amazon",
					"Troque pontos por cupons de até 20% na Amazon.",
					R.drawable.amazon_logo
				),
				Partner(
					"mercado_livre",
					"Mercado Livre",
					"Pontos que geram descontos e frete grátis.",
					R.drawable.mercadolivre_logo
				),
				Partner(
					"ifood",
					"Ifood",
					"Descontos de até 10% em pedidos parceiros.",
					R.drawable.ifood_logo
				),
				Partner(
					"fiap",
					"FIAP",
					"Pontos trocáveis por cursos e descontos educativos.",
					R.drawable.fiap_logo
				),
				Partner(
					"uber",
					"Uber",
					"Ganhe créditos de viagem em campanhas de reciclagem.",
					R.drawable.uber_logo
				)
			)

			partners.forEach { partner ->
				Card(
					modifier = Modifier
						.fillMaxWidth()
						.padding(vertical = 8.dp),
					shape = RoundedCornerShape(12.dp),
					colors = CardDefaults.cardColors(containerColor = WHITE.copy(alpha = 0.06f)),
					elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
				) {
					Row(
						modifier = Modifier
							.fillMaxWidth()
							.padding(16.dp),
						verticalAlignment = Alignment.CenterVertically,
						horizontalArrangement = Arrangement.SpaceBetween
					) {
						if (partner.logoRes != null) {
							Box(
								modifier = Modifier
									.size(64.dp)
									.clip(RoundedCornerShape(12.dp))
									.background(color = WHITE.copy(alpha = 0.06f)),
								contentAlignment = Alignment.Center
							) {
								Image(
									painter = painterResource(id = partner.logoRes),
									contentDescription = partner.name,
									modifier = Modifier
										.padding(8.dp)
										.fillMaxSize(),
									contentScale = ContentScale.Fit
								)
							}
						} else {
							Box(
								modifier = Modifier
									.size(64.dp)
									.clip(RoundedCornerShape(12.dp))
									.background(color = WHITE.copy(alpha = 0.06f)),
								contentAlignment = Alignment.Center
							) {
								Text(text = partner.name.take(1), color = WHITE, fontWeight = FontWeight.Bold)
							}
						}

						Spacer(modifier = Modifier.width(12.dp))

						Column(modifier = Modifier.weight(1f)) {
							Text(text = partner.name, color = WHITE, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
							Spacer(modifier = Modifier.height(6.dp))
							Text(
								text = partner.benefit,
								color = WHITE,
								fontSize = 14.sp,
								maxLines = 2,
								overflow = TextOverflow.Ellipsis
							)
						}

						Spacer(modifier = Modifier.width(12.dp))

						Button(
							onClick = { onAction(partner.id) },
							colors = ButtonDefaults.buttonColors(containerColor = BLUE),
							modifier = Modifier.shadow(elevation = 8.dp, shape = RoundedCornerShape(8.dp), spotColor = BLUE)
						) {
							Text(text = "Resgatar", color = WHITE, fontWeight = FontWeight.Bold)
						}
					}
				}
			}
		}

		Column(
			modifier = Modifier
				.fillMaxWidth()
				.padding(20.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Button(
				onClick = { onBack() },
				modifier = Modifier
					.fillMaxWidth()
					.height(48.dp),
				colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
				border = BorderStroke(width = 1.dp, color = WHITE)
			) {
				Text(text = "Voltar", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = WHITE)
			}

			Spacer(modifier = Modifier.height(12.dp))

			Button(
				onClick = { onAction("geral") },
				modifier = Modifier
					.fillMaxWidth()
					.height(48.dp),
				colors = ButtonDefaults.buttonColors(containerColor = BLUE),
				shape = RoundedCornerShape(24.dp),
				elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
			) {
				Text(text = "Seja parceiro", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = WHITE)
			}
		}

	}
}

@Preview
@Composable
fun PartnersScreenPreview() {
	PartnersScreen()
}


