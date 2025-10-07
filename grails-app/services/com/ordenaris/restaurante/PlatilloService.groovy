package com.ordenaris.restaurante

import grails.gorm.transactions.Transactional

@Transactional
class PlatilloService {

    def listaPlatillos(uuid) {
        try {
            // select * from tipo_menu where uuid = 'uuid';
            def tipoPlatillo = TipoMenu.findByUuid(uuid)
            if (!tipoPlatillo) {
                return [
                    resp: [ success: false, mensaje: "Tipo de platillo no encontrado" ],
                    status: 404
                ]
            }

            // select * from platillo where tipo_menu_id = tipoPlatillo.id and status != 2;
            def list = Platillo.findAllByTipoMenuAndStatusNotInList(tipoPlatillo,[0,2])

            if (!list || list.empty) {
                return [
                    resp: [ success: false, mensaje: "No hay platillos disponibles para este tipo de menú" ],
                    status: 404
                ]
            }

            def lista = list.collect { platillo -> 
            return [
                nombre: platillo.nombre,
                descripcion: platillo.descripcion,
                costo: platillo.costo,
                platillosDisponibles: platillo.platillosDisponibles,
                fechaDisponible: platillo.fechaDisponible,
                estatus: platillo.status,
                tipoMenu: [
                    nombre: platillo.tipoMenu?.nombre,
                    uuid: platillo.tipoMenu?.uuid

                ]
            ]
            }   
            

            return [
                resp: [ success: true, data: lista ],
                status: 200
            ]

        } catch(e) {
            return [
                resp: [ success: false, mensaje: e.getMessage() ],
                status: 500
            ]
        }
    }

    def mapPlatillo = { platillo, lista ->
        def obj = [
            nombre: platillo.nombre,
            descripcion: platillo.descripcion,
            costo: platillo.costo,
            platillosDisponibles: platillo.platillosDisponibles,
            fechaDisponible: platillo.fechaDisponible,
            status: platillo.status,
            uuid: platillo.uuid,
            tipoMenu: [
                nombre: platillo.tipoMenu?.nombre,
                uuid: platillo.tipoMenu?.uuid
            ]
        ]
        if (lista.size() > 0) {
            obj.submenu = lista
        }
        return obj
    }

    def nuevoPlatillo(uuid, data) {
        try {
            // select * from tipo_menu where uuid = 'uuid';
            def tipoPlatillo = TipoMenu.findByUuid(uuid)
            if (!tipoPlatillo) {
                return [
                    resp: [ success: false, mensaje: "Tipo de platillo no encontrado" ],
                    status: 404
                ]
            }
        
            Date fechaDisponible = null
            def hoy = new Date()
            def estadoInicial = 1

            if(data.fechaDisponible){
                if(!(data.fechaDisponible ==~ /^\d{4}-\d{2}-\d{2}$/)){
                    return [
                        resp: [ success: false, mensaje: "La fecha disponible debe tener el formato yyyy-MM-dd" ],
                        status: 400
                    ]
                } else {
                    fechaDisponible = Date.parse("yyyy-MM-dd", data.fechaDisponible)
                }

                if(fechaDisponible && fechaDisponible.after(hoy)){
                    estadoInicial =0
                }
            }
            def nuevo = new Platillo([
                nombre: data.nombre,
                costo: data.costo,
                descripcion: data.descripcion ?: "",
                platillosDisponibles: data.platillosDisponibles ?: -1,
                fechaDisponible: fechaDisponible, 
                status: estadoInicial,
                tipoMenu: tipoPlatillo
            ]).save(flush: true, failOnError: true)

            return [
                resp: [ success: true, mensaje: "Platillo registrado", uuid: nuevo.uuid ],
                status: 201
            ]

        } catch(e) {
            return [
                resp: [ success: false, mensaje: e.getMessage() ],
                status: 500
            ]
        }
    }

    def editarPlatillo( uuid, data) {
        try {
            // select * from platillo where uuid = 'uuid';
            def platillo = Platillo.findByUuid(uuid)
            if (!platillo) {
                return [
                    resp: [ success: false, mensaje: "Platillo no encontrado" ],
                    status: 404
                ]
            }

            platillo.nombre = data.nombre ?: platillo.nombre
            platillo.costo = data.costo ?: platillo.costo
            platillo.descripcion = data.descripcion ?: platillo.descripcion
            platillo.platillosDisponibles = data.platillosDisponibles ?: platillo.platillosDisponibles
            platillo.fechaDisponible = data.fechaDisponible ? Date.parse("yyyy-MM-dd", data.fechaDisponible) : platillo.fechaDisponible

            platillo.save(flush: true, failOnError: true)
            return [
                resp: [ success: true, mensaje: "Platillo actualizado"],
                status: 200
            ]

        } catch(e) {
            return [
                resp: [ success: false, mensaje: e.getMessage() ],
                status: 500
            ]
        }
    }

    def editarEstatusPlatillo( estatus,  uuid) {
        try {
            // select * from platillo where uuid = 'uuid';
            def platillo = Platillo.findByUuid(uuid)
            if (!platillo) {
                return [
                    resp: [ success: false, mensaje: "Platillo no encontrado" ],
                    status: 404
                ]
            }

            platillo.status = estatus
            platillo.save()

            return [
                resp: [ success: true, mensaje: "Estatus actualizado" ],
                status: 200
            ]

        } catch(e) {
            return [
                resp: [ success: false, mensaje: e.getMessage() ],
                status: 500
            ]
        }
    }

    
    def informacionPlatillo( uuid) {
        try {
            // select * from platillo where uuid = 'uuid';
            def platillo = Platillo.findByUuid(uuid)
            if (!platillo) {
                return [
                    resp: [ success: false, mensaje: "Platillo no encontrado" ],
                    status: 404
                ]
            }
            if (platillo.status in [0,2]) {
                return[
                    resp:[success:false, mensaje: "El platillo no existe"],
                    status: 404
                ]
            }

           def data = [
            uuid: platillo.uuid,
            nombre: platillo.nombre,
            descripcion: platillo.descripcion,
            costo: platillo.costo,
            platillosDisponibles: platillo.platillosDisponibles,
            fechaDisponible: platillo.fechaDisponible,
            estatus: platillo.status,
            tipoMenu: [
                nombre: platillo.tipoMenu?.nombre,
                uuid: platillo.tipoMenu?.uuid
            ]
        ]
            return [
                resp: [ success: true, data: data ],
                status: 200
            ]

        } catch(e) {
            return [
                resp: [ success: false, mensaje: e.getMessage() ],
                status: 500
            ]
        }
    }




    def paginarPlatillos(String uuid, Integer pagina, String columnaOrden, String orden, Integer max, Integer estatus, String query) {
        try {
            // select * from tipo_menu where uuid = 'uuid';
            def tipoMenu = TipoMenu.findByUuid(uuid)
            if (!tipoMenu) {
                return [
                    resp: [ success: false, mensaje: "El tipo de menú no existe" ],
                    status: 404
                ]
            }

            def offset = (pagina * max) - max

            def resultados = Platillo.createCriteria().list {
                eq("tipoMenu", tipoMenu)
                if (estatus != null) {
                    eq("status", estatus)
                } else {
                    ne("status", 2)
                }
                if (query) {
                    ilike("nombre", "%${query}%")
                }
                firstResult(offset)
                maxResults(max)
                order(columnaOrden, orden)
            }.collect { platillo -> mapPlatillo(platillo, []) }

            return [
                resp: [ success: true, data: resultados ],
                status: 200
            ]

        } catch(e) {
            return [
                resp: [ success: false, mensaje: e.getMessage() ],
                status: 500
            ]
        }
    }
}
