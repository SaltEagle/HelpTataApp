package com.example.helptataapp.util

// ═══════════════════════════════════════════════════════════════════════════
//  RutUtils.kt  —  Utilidades de validación y formato para el registro
//
//  Incluye:
//   • Formateo automático de RUT chileno (12.345.678-9)
//   • Validación del dígito verificador (algoritmo módulo 11)
//   • Validación de nombres y apellidos (solo letras y espacios)
//   • Validación de teléfono chileno (9 dígitos, empieza en 9)
//   • Evaluación de fortaleza de contraseña
// ═══════════════════════════════════════════════════════════════════════════

// ── RUT ─────────────────────────────────────────────────────────────────────

/**
 * Formatea el RUT completo mientras el usuario escribe.
 * Entrada esperada: cualquier texto (el usuario escribe libre)
 * Salida:  12.345.678-9  o  12.345.678-K
 *
 * Lógica:
 *  1. Quita todo lo que no sea dígito o K/k
 *  2. Separa los últimos caracteres como DV
 *  3. Aplica puntos cada 3 dígitos al cuerpo
 *  4. Une con guión
 */
fun formatearRut(raw: String): String {
    val limpio = raw.uppercase().filter { it.isDigit() || it == 'K' }
    if (limpio.isEmpty()) return ""

    return if (limpio.length >= 8) {
        // Ya tiene body + DV → poner guión antes del último char
        val dv = limpio.last().toString()
        val cuerpo = limpio.dropLast(1)
        val conPuntos = cuerpo.reversed().chunked(3).joinToString(".").reversed()
        "$conPuntos-$dv"
    } else {
        // Formateo progresivo del cuerpo (DV aún no ingresado)
        // "1234" → "1.234",  "12345" → "12.345",  "1234567" → "1.234.567"
        limpio.reversed().chunked(3).joinToString(".").reversed()
    }
}

/**
 * Extrae solo los dígitos del cuerpo del RUT (sin DV, sin puntos ni guión).
 * Usado para enviar a la API.
 */
fun rutSinFormato(rutFormateado: String): String =
    rutFormateado.filter { it.isDigit() }.dropLast(0).let {
        // quitar el último si es K o dígito del DV
        val clean = rutFormateado.uppercase().filter { c -> c.isDigit() || c == 'K' }
        if (clean.length > 1) clean.dropLast(1) else ""
    }

/**
 * Extrae el dígito verificador del RUT formateado.
 */
fun dvDeRut(rutFormateado: String): String {
    val clean = rutFormateado.uppercase().filter { it.isDigit() || it == 'K' }
    return if (clean.isNotEmpty()) clean.last().toString() else ""
}

/**
 * Valida el dígito verificador usando el algoritmo módulo 11 chileno.
 * Retorna true si el RUT completo (cuerpo + DV) es válido.
 *
 * @param rutFormateado  RUT en cualquier formato (con o sin puntos/guión)
 */
fun validarRut(rutFormateado: String): Boolean {
    val clean = rutFormateado.uppercase().filter { it.isDigit() || it == 'K' }
    if (clean.length < 2) return false

    val dvIngresado = clean.last().toString()
    val cuerpo = clean.dropLast(1)

    if (cuerpo.isEmpty() || !cuerpo.all { it.isDigit() }) return false
    val numero = cuerpo.toLongOrNull() ?: return false
    if (numero < 1_000_000L || numero > 99_999_999L) return false  // rango válido

    // Algoritmo módulo 11
    var suma    = 0
    var factor  = 2
    var n       = numero
    while (n > 0) {
        suma   += (n % 10).toInt() * factor
        n      /= 10
        factor  = if (factor == 7) 2 else factor + 1
    }
    val dvCalculado = when (val resto = 11 - (suma % 11)) {
        11   -> "0"
        10   -> "K"
        else -> resto.toString()
    }
    return dvIngresado == dvCalculado
}

// ── Nombres y Apellidos ──────────────────────────────────────────────────────

/**
 * Permite solo letras (incluyendo acentos, ñ, ü) y espacios.
 * Elimina silenciosamente cualquier otro carácter.
 * Máximo 50 caracteres.
 */
fun filtrarNombre(input: String): String =
    input
        .filter { it.isLetter() || it == ' ' }
        .take(50)

/**
 * Valida que el nombre no esté vacío y tenga al menos 2 caracteres reales.
 */
fun validarNombre(nombre: String): String? = when {
    nombre.isBlank()      -> "Este campo es obligatorio."
    nombre.trim().length < 2 -> "Debe tener al menos 2 caracteres."
    else                  -> null   // null = sin error
}

// ── Teléfono ────────────────────────────────────────────────────────────────

/**
 * Filtra el teléfono: solo dígitos, máximo 9 caracteres.
 * El primer dígito debe ser 9 (celular chileno).
 */
fun filtrarTelefono(input: String): String {
    val soloDigitos = input.filter { it.isDigit() }.take(9)
    // Si el usuario escribe algo que no empieza en 9, lo ignoramos
    return if (soloDigitos.isNotEmpty() && soloDigitos[0] != '9') {
        soloDigitos.drop(1)   // quita el primer char inválido
    } else {
        soloDigitos
    }
}

/**
 * Valida el teléfono celular chileno.
 * Debe tener exactamente 9 dígitos y empezar en 9.
 */
fun validarTelefono(telefono: String): String? = when {
    telefono.isBlank()       -> "El teléfono es obligatorio."
    !telefono.startsWith("9") -> "Debe empezar con 9 (celular chileno)."
    telefono.length < 9      -> "Debe tener 9 dígitos. Ejemplo: 912345678"
    else                     -> null
}

// ── Contraseña ───────────────────────────────────────────────────────────────

enum class PasswordStrength(val label: String, val color: Long) {
    MUY_DEBIL("Muy débil",  0xFFDC2626),   // rojo
    DEBIL     ("Débil",     0xFFF97316),   // naranja
    MEDIA     ("Aceptable", 0xFFF4A732),   // ámbar
    FUERTE    ("Fuerte",    0xFF28A745),   // verde
}

/**
 * Evalúa la fortaleza de la contraseña.
 * Criterios: longitud, mayúsculas, dígitos, símbolos.
 */
fun evaluarContrasena(password: String): PasswordStrength {
    if (password.length < 6) return PasswordStrength.MUY_DEBIL
    var puntos = 0
    if (password.length >= 8)                    puntos++
    if (password.any { it.isUpperCase() })       puntos++
    if (password.any { it.isDigit() })           puntos++
    if (password.any { !it.isLetterOrDigit() })  puntos++
    return when (puntos) {
        0, 1 -> PasswordStrength.DEBIL
        2    -> PasswordStrength.MEDIA
        else -> PasswordStrength.FUERTE
    }
}

/**
 * Valida la contraseña con reglas básicas de seguridad.
 */
fun validarContrasena(password: String): String? = when {
    password.isBlank()   -> "La contraseña es obligatoria."
    password.length < 6  -> "Debe tener al menos 6 caracteres."
    password.contains(" ") -> "No puede contener espacios."
    else                 -> null
}
