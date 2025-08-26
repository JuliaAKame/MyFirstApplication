package com.fiap.myapp.screens


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
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

val MinhaFonte = FontFamily(
    Font(R.font.righteous_regular)
)
@Composable
fun Login() {
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
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier
                .width(300.dp)
                .height(300.dp),
            alignment = Alignment.Center
        )
        Text(
            text = buildAnnotatedString {
                append("CLEAN")
                withStyle(
                    style = SpanStyle(
                    color = BLUE,
                        fontFamily = MinhaFonte
                    )
                ) {
                    append("WORLD\n")
                }
            },
            color = WHITE,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxSize().padding(20.dp)


        )
        Text(
            text = "Cuidar do meio ambiente é mais do que uma responsabilidade, é uma forma de causar impacto positivo e transformar o futuro",
            color = WHITE,
            fontSize = 17.sp,
            modifier = Modifier.fillMaxSize().padding(20.dp,20.dp,20.dp,30.dp)

        )
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
        ) {
            Button(
                onClick ={

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = WHITE
                )
            ) {
                Text(
                    text = "Fazer Login",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = WHITE
                )
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    onClick ={

                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BLUE
                    ),
                    modifier = Modifier.shadow(
                        elevation = 16.dp,
                        shape = CircleShape,
                        spotColor = BLUE
                    )

                ) {
                    Text(
                        text = "Cadastre-se",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = WHITE
                    )
                }
            }
        }

    }
}

@Preview
@Composable
fun LoginPreview() {
    Login()
}