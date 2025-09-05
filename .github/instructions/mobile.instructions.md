# 🌱 CleanWorld - Feature de Registro de Materiais Recicláveis

## 📋 Visão Geral
Feature para permitir que usuários registrem materiais recicláveis através de foto e descrição, ganhando pontos no aplicativo.

## 🎯 Objetivos da Feature
- Incentivar a reciclagem através de gamificação
- Validar contribuições dos usuários 
- Integrar com o sistema de pontos existente
- Manter a experiência consistente com o design atual

## 🔄 Fluxo do Usuário

### 1. Acesso à Feature
- Usuário autenticado acessa nova tela "Registrar Reciclagem"
- Disponível através de nova rota no menu principal

### 2. Processo de Registro
1. **Captura de Foto**: Usuário tira foto do material reciclável
2. **Descrição**: Preenche campo de texto descritivo
3. **Categoria**: Seleciona tipo de material (Plástico, Vidro, Papel, Metal, Orgânico)
4. **Estimativa de Peso**: Seleciona faixa de peso aproximado
5. **Validação**: Sistema valida os dados inseridos
6. **Pontuação**: Se válido, usuário recebe pontos

### 3. Sistema de Pontuação
- **Plástico**: 10-50 pontos (conforme peso)
- **Vidro**: 15-60 pontos
- **Papel**: 5-30 pontos
- **Metal**: 20-80 pontos
- **Orgânico**: 8-40 pontos

## 🛠 Arquitetura Técnica

### Estrutura de Pastas
```
recycling/
├── RecyclingState.kt          # Estados da feature
├── RecyclingRepository.kt     # Interface e implementação
├── RecyclingViewModel.kt      # ViewModel da feature
└── models/
    ├── RecyclingItem.kt       # Modelo de item reciclável
    ├── MaterialCategory.kt    # Enum de categorias
    └── WeightRange.kt         # Enum de faixas de peso

screens/
├── RecyclingScreen.kt         # Tela principal de registro
└── RecyclingHistoryScreen.kt  # Histórico de registros

navigation/
└── Routes.kt                  # Adição das novas rotas
```

### Validações Implementadas
1. **Foto obrigatória** - Verificação de presença da imagem
2. **Descrição mínima** - Pelo menos 10 caracteres
3. **Palavras-chave** - Validação por categoria de material
4. **Limite diário** - Máximo 5 registros por usuário/dia

### Persistência de Dados
- Firebase Firestore para armazenar registros
- Firebase Storage para imagens
- Cache local para melhor performance

## 🎨 Design System

### Componentes Reutilizáveis
- `CategorySelector`: Seletor de categoria de material
- `WeightSelector`: Seletor de faixa de peso
- `CameraCapture`: Componente de captura de foto
- `PointsDisplay`: Exibição de pontos ganhos
- `ValidationMessage`: Mensagens de validação

### Paleta de Cores
- **Verde (GREEN)**: Ações positivas, sucesso
- **Azul (BLUE)**: Ações primárias, botões
- **Branco (WHITE)**: Texto e elementos de destaque
- **Vermelho**: Validações de erro

## 📱 Integração com App Existente

### Navegação
1. Nova opção no menu principal: "Registrar Reciclagem"
2. Histórico acessível via "Meus Registros"
3. Integração com tela de Partners (pontos acumulados)

### Compatibilidade
- Mantém arquitetura Clean Code implementada
- Utiliza mesmo padrão Repository + ViewModel
- Segue convenções de nomenclatura em inglês
- Integra com sistema de autenticação existente

## 🔧 Implementação Técnica

### Dependências Necessárias
```kotlin
// Camera
implementation "androidx.camera:camera-camera2:1.4.1"
implementation "androidx.camera:camera-lifecycle:1.4.1" 
implementation "androidx.camera:camera-view:1.4.1"

// Permissions
implementation "androidx.activity:activity-compose:1.9.2"
implementation "com.google.accompanist:accompanist-permissions:0.32.0"
```

### Permissões (AndroidManifest.xml)
```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

## 📊 Métricas e Analytics
- Total de itens registrados por usuário
- Categorias mais populares
- Pontos distribuídos por categoria
- Taxa de validação (aprovação vs rejeição)

## 🚀 Roadmap de Desenvolvimento

### Fase 1 (MVP)
- [x] Estrutura base da feature
- [x] Tela de registro básica
- [x] Sistema de validação simples
- [x] Integração com pontos

### Fase 2 (Melhorias)
- [ ] IA para validação de imagens
- [ ] Geolocalização dos registros
- [ ] Sistema de ranking entre usuários
- [ ] Notificações push para incentivo

### Fase 3 (Avançado)
- [ ] Reconhecimento automático de materiais
- [ ] Parcerias com cooperativas locais
- [ ] Marketplace de pontos ampliado
- [ ] Relatórios de impacto ambiental

## 🎮 Gamificação

### Sistema de Conquistas
- **Primeira Reciclagem**: 50 pontos bônus
- **Eco Warrior**: 100 registros válidos (500 pontos)
- **Diversificado**: Registros em todas as categorias (200 pontos)
- **Consistente**: 30 dias seguidos reciclando (300 pontos)

### Níveis de Usuário
1. **Iniciante**: 0-500 pontos
2. **Consciente**: 500-1500 pontos  
3. **Ativista**: 1500-5000 pontos
4. **Eco Hero**: 5000+ pontos

---

*Este documento será atualizado conforme o desenvolvimento da feature evolui.*
