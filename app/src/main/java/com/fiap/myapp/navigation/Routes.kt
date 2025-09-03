package com.fiap.myapp.navigation

/**
 * Definição de todas as rotas da aplicação
 */
object Routes {
    const val LOGIN = "login"
    const val CADASTRO = "cadastro"
    const val PARTNERS = "partners"
    
    /**
     * Rota inicial baseada no estado de autenticação
     */
    fun getStartRoute(isAuthenticated: Boolean): String {
        return if (isAuthenticated) PARTNERS else LOGIN
    }
}
