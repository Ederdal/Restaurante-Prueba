package restaurante

class UrlMappings {

    static mappings = {
        group "/menu", {
            group "/tipo",/*tipo de menu*/{
                // Ruta: /menu/tipo/nuevo
                post "/nuevo"(controller: "menu", action: "nuevoTipo")
                get "/lista"(controller: "menu", action: "listaTipos")
                get "/ver"(controller: "menu", action: "paginarTipos")
                group "/$uuid", {
                    get "/informacion"(controller: "menu", action: "informacionTipo")
                    patch "/editar"(controller: "menu", action: "editarTipo")
                    patch "/activar"(controller: "menu", action: "editarEstatusTipo"){
                        estatus = 1
                    }
                    patch "/desactivar"(controller: "menu", action: "editarEstatusTipo") {
                        estatus = 0
                    }
                    patch "/eliminar"(controller: "menu", action: "editarEstatusTipo") {
                        estatus = 2
                    }

                }

            }  
                group "/$uuid", {/* uuid del tipo de menu*/

                        group "/platillo", /* platillos del menu*/{
                            // Ruta ejemplo: menu/uuid_menu/platillo/nuevo
                            post "/nuevo"(controller: "platillo", action: "nuevoPlatillo")
                            get "/lista"(controller: "platillo", action: "listaPlatillos")  
                            get "/ver"(controller: "platillo", action: "paginarPlatillos")
                    
                    }
                } 

            group "/platillo", {
                        group "/$uuid", {/* uuid del platillo*/
                        // Ruta ejemplo : menu/platillo/uuid_platillo/editar
                            get "/informacion"(controller: "platillo", action: "informacionPlatillo")
                            patch "/editar"(controller: "platillo", action: "editarPlatillo")
                            patch "/activar"(controller: "platillo", action: "editarEstatusPlatillo"){
                            estatus = 1
                            }
                            patch "/desactivar"(controller: "platillo", action: "editarEstatusPlatillo"){
                            estatus = 0
                            }
                            patch "/eliminar"(controller: "platillo", action: "editarEstatusPlatillo"){
                            estatus = 2
                            }
                        }
             }
        }

        "/"(controller: 'application', action:'index')
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
