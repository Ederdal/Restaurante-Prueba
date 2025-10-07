package com.ordenaris.restaurante

import grails.converters.JSON

class PlatilloController {

    static responseFormats = ['json', 'xml']
    def platilloService  

    // Lista platillos por tipo de menú
    def listaPlatillos(String uuid) {
      
        def respuesta = platilloService.listaPlatillos(uuid)
        return respond( respuesta.resp, status: respuesta.status )
    }

    // Crea un nuevo platillo en el tipo de menú
    def nuevoPlatillo(String uuid) {
        def data = request.JSON
        // Validaciones
        if(!uuid || uuid.size() != 32 ) {
            return respond([success: false, mensaje: "El uuid es invalido"], status: 400)
        }
        def tipoMenu = TipoMenu.findByUuid(uuid)
        if (!tipoMenu) {
            return respond([success: false, mensaje: "El tipo de menu no existe o fue eliminado"], status: 404)
        }

        if (!data) {
            return respond([success: false, mensaje: "No se recibieron datos del platillo"], status: 400)
        }
        if (!data.nombre) {
            return respond([success: false, mensaje: "El nombre del platillo es obligatorio"], status: 400)
        }
        if (data.nombre.soloNumeros()) {
            return respond([success: false, mensaje: "El nombre debe contener letras y no solo numeros"], status: 400)
        }
        if (data.nombre.size() > 80) {
            return respond([success: false, mensaje: "El nombre no puede superar los 80 caracteres"], status: 400)
        }
        if (!data.descripcion){
            return respond([success: false, mensaje:"La descripcion no puede ir vacia"])
        }
        if( data.descripcion.soloNumeros()){
            return respond([success: false, mensaje: "La descripcion debe tener letras y no solo numeros"])
        }

        if (data.costo != null ) {
            try {
                def costo = data.costo as Double
                if (costo < 0 || costo > 600) {
                    return respond([success: false, mensaje: "El costo debe estar entre 0 y 600"], status: 400)
                }
            } catch (e) {
                return respond([success: false, mensaje: "El costo debe ser numerico"], status: 400)
            }
        }
        if (!data.costo){
            return respond([success:false, mensaje:"El costo es obligatorio"])
        }

        if (data.fechaDisponible && !(data.fechaDisponible ==~ /^\d{4}-\d{2}-\d{2}$/)) {
            return respond([success: false, mensaje: "La fecha disponible debe tener el formato yyyy-MM-dd"], status: 400)
        }

        if (data.platillosDisponibles != null && !(data.platillosDisponibles instanceof Integer)) {
            return respond([success: false, mensaje: "El campo platillosDisponibles debe ser numerico"], status: 400)
        }


        def respuesta = platilloService.nuevoPlatillo(uuid, data)
        return respond(respuesta.resp, status: respuesta.status)
}


   // Edita un platillo existente por su UUID
    def editarPlatillo(String uuid) {
        def data = request.JSON

        // Validar UUID
        if (!uuid || uuid.size() != 32) {
            return respond([success: false, mensaje: "El uuid es invalido"], status: 400)
        }

        // Validar nombre (solo si viene)
        if (data.nombre) {
            if (data.nombre.soloNumeros()) {
                return respond([success: false, mensaje: "El nombre debe contener letras y no solo numeros"], status: 400)
            }

            if (data.nombre.size() > 80) {
                return respond([success: false, mensaje: "El nombre no puede superar los 80 caracteres"], status: 400)
            }
        }

        // Validar costo (solo si viene)
        if (data.costo != null) {
            try {
                def costo = data.costo as Double
                if (costo < 0 || costo > 600) {
                    return respond([success: false, mensaje: "El costo debe estar entre 0 y 600"], status: 400)
                }
            } catch (e) {
                return respond([success: false, mensaje: "El costo debe ser numerico"], status: 400)
            }
        }

        // Validar platillosDisponibles (solo si viene)
        if (data.platillosDisponibles != null && !(data.platillosDisponibles instanceof Integer)) {
            return respond([success: false, mensaje: "El campo platillosDisponibles debe ser numerico"], status: 400)
        }
            // Llamar al servicio
        def respuesta = platilloService.editarPlatillo(uuid, data)
        return respond(respuesta.resp, status: respuesta.status)
}


// Cambia el estatus de un platillo (activar, desactivar, eliminar)
def editarEstatusPlatillo(String uuid) {
    if (!params.uuid) {
        return respond([success:false, mensaje:"El UUID es obligatorio"], status:400)
    }
    if (params.uuid.size() != 32) {
        return respond([success:false, mensaje:"El UUID debe tener 32 caracteres"], status:400)
    }
   
    // Solo permitimos ciertos valores de estatus
    def estatusValido = params.estatus.toInteger()
    if (!(estatusValido in [0, 1, 2])) {
        return respond([success:false, mensaje:"El estatus solo puede ser: 0 (inactivo), 1 (activo), 2 (eliminado)"], status:400)
    }

    def respuesta = platilloService.editarEstatusPlatillo(estatusValido, params.uuid)
    return respond(respuesta.resp, status: respuesta.status)
}

// Información de un platillo por su UUID
def informacionPlatillo(String uuid) {
    if (!uuid) {
        return respond([success:false, mensaje:"El UUID es obligatorio"], status:400)
    }
    if (uuid.size() != 32) {
        return respond([success:false, mensaje:"El UUID debe tener 32 caracteres"], status:400)
    }

    def respuesta = platilloService.informacionPlatillo(uuid)
    return respond(respuesta.resp, status: respuesta.status)
}

    // Paginación de platillos por tipo de menú
    def paginarPlatillos(String uuid){

       if( !params.pagina ) {
            return respond([success:false, mensaje: "La pagina no puede ir vacio"], status: 400)
        }
        if( !params.pagina.soloNumeros() ) {
            return respond([success:false, mensaje: "La pagina debe contener solo numeros"], status: 400)
        }
        
        if( !params.columnaOrden ) {
            return respond([success:false, mensaje: "El columnaOrden no puede ir vacio"], status: 400)
        }
        if( !(params.columnaOrden in ["nombre", "status", "dateCreated"]) ) {
            return respond([success:false, mensaje: "El columnaOrden solo puede ser: nombre, status, dateCreated"], status: 400)
        }

        if( !params.orden ) {
            return respond([success:false, mensaje: "El orden no puede ir vacio"], status: 400)
        }
        if( !(params.orden in ["asc", "desc"]) ) {
            return respond([success:false, mensaje: "El orden solo puede ser: asc, desc"], status: 400)
        }

        if( !params.max ) {
            return respond([success:false, mensaje: "El max no puede ir vacio"], status: 400)
        }
        if( !params.max.soloNumeros() ) {
            return respond([success:false, mensaje: "El max debe contener solo numeros"], status: 400)
        }
        if( !(params.max.toInteger() in [ 2, 5, 10, 20, 50, 100 ]) ) {
            return respond([success:false, mensaje: "El max puede ser solo: 2, 5, 10, 20, 50, 100"], status: 400)
        }
        
        def respuesta = platilloService.paginarPlatillos( uuid, params.pagina.toInteger(), params.columnaOrden, params.orden, params.max.toInteger(), params.estatus?.toInteger(), params.query )
        return respond( respuesta.resp, status: respuesta.status )
    }
     


}
