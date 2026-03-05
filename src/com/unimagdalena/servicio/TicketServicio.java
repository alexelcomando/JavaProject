package com.unimagdalena.servicio;

import com.poo.persistence.NioFile;
import com.unimagdalena.api.ApiOperacionBD;
import com.unimagdalena.controlador.cliente.ClienteControladorListar;
import com.unimagdalena.dto.ClienteDto;
import com.unimagdalena.dto.TicketDto;
import com.unimagdalena.model.Cliente;
import com.unimagdalena.model.Ticket;
import com.unimagdalena.helpers.constante.Persistencia;
import com.unimagdalena.helpers.utilidad.GestorImagen;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TicketServicio implements ApiOperacionBD<TicketDto> {

    private NioFile miArchivo;
    private String nombrePersistencia;

    public TicketServicio() {
        try {
            nombrePersistencia = Persistencia.NOMBRE_TICKET;
            miArchivo = new NioFile(nombrePersistencia);
        } catch (IOException ex) {
            Logger.getLogger(TicketServicio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<TicketDto> SelectFrom() {
        List<TicketDto> arregloTickets = new ArrayList<>();
        List<String> arregloDatos = miArchivo.obtenerDatos();

        for (String cadena : arregloDatos) {
            try {
                cadena = cadena.replace("@", "");
                String[] columnas = cadena.split(Persistencia.SEPARADOR_COLUMNAS);

                int idTicket = Integer.parseInt(columnas[0].trim());
                String tipoEntrada = columnas[1].trim();

                // Buscar el cliente
                ClienteDto clienteDto = null;
                int idCliente = Integer.parseInt(columnas[2].trim());
                for (ClienteDto obj : ClienteControladorListar.obtenerTodos()) {
                    if (obj.getIdCliente() == idCliente) {
                        clienteDto = obj;
                        break;
                    }
                }

                Boolean esValido = Boolean.valueOf(columnas[3].trim());
                LocalDate fechaEmision = LocalDate.parse(columnas[4].trim());
                Double precio = Double.parseDouble(columnas[5].trim());
                String imagenPublica = columnas[6].trim();
                String imagenPrivada = columnas[7].trim();

                arregloTickets.add(new TicketDto(
                        idTicket,
                        tipoEntrada,
                        clienteDto,
                        esValido,
                        fechaEmision,
                        precio,
                        imagenPublica,
                        imagenPrivada
                ));

            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.println("Error al leer ticket: " + e.getMessage());
            }
        }
        return arregloTickets;
    }

    @Override
  public TicketDto insertInto(TicketDto dto, String ruta) {
    Ticket objTicket = new Ticket();

    // ---- Conversión ClienteDto -> Cliente ----
    Cliente objCliente = null;

    if (dto.getIdClienteTicket() != null) {

        ClienteDto clienteDto = dto.getIdClienteTicket();

        // Convertir PeliculaDto a Pelicula (modelo)
        com.unimagdalena.model.Pelicula objPelicula = null;

        if (clienteDto.getIdPeliculaCliente() != null) {
            var peliDto = clienteDto.getIdPeliculaCliente();

            objPelicula = new com.unimagdalena.model.Pelicula();
            objPelicula.setIdPelicula(peliDto.getIdPelicula());
            objPelicula.setNombrePelicula(peliDto.getNombrePelicula());
        }

        objCliente = new Cliente(
                clienteDto.getIdCliente(),
                clienteDto.getNombreCliente(),
                clienteDto.getTipoDocumentoCliente(),
                clienteDto.getFechaDeNacimientoCliente(),
                clienteDto.getSexoCliente(),
                clienteDto.getEsAccesoVipCliente(),
                objPelicula,
                clienteDto.getNombreImagenPublicoCliente(),
                clienteDto.getNombreImagenPrivadoCliente()
        );
    }

    // ---- Asignar valores al Ticket ----
    objTicket.setIdTicket(getSerial());
    objTicket.setTipoEntradaTicket(dto.getTipoEntradaTicket());
    objTicket.setIdClienteTicket(objCliente);
    objTicket.setEsValidoTicket(dto.getEsValidoTicket());
    objTicket.setFechaEmisionTicket(dto.getFechaEmisionTicket());
    objTicket.setPrecioTicket(dto.getPrecioTicket());
    objTicket.setNombreImagenPublicoTicket(dto.getNombreImagenPublicoTicket());
    objTicket.setNombreImagenPrivadoTicket(GestorImagen.grabarLaImagen(ruta));

    // ---- ARMAR CADENA DE PERSISTENCIA ----
    String cadena =
            objTicket.getIdTicket() + Persistencia.SEPARADOR_COLUMNAS +
            objTicket.getTipoEntradaTicket() + Persistencia.SEPARADOR_COLUMNAS +
            (objCliente != null ? objCliente.getIdCliente() : 0) + Persistencia.SEPARADOR_COLUMNAS +
            objTicket.getEsValidoTicket() + Persistencia.SEPARADOR_COLUMNAS +
            objTicket.getFechaEmisionTicket() + Persistencia.SEPARADOR_COLUMNAS +
            new BigDecimal(String.valueOf(objTicket.getPrecioTicket())).toPlainString() + Persistencia.SEPARADOR_COLUMNAS +
            objTicket.getNombreImagenPublicoTicket() + Persistencia.SEPARADOR_COLUMNAS +
            objTicket.getNombreImagenPrivadoTicket();

    // ---- GUARDAR ----
    if (miArchivo.agregarRegistro(cadena)) {
        dto.setIdTicket(objTicket.getIdTicket());
        return dto;
    }

    return null;
}


    @Override
    public int getSerial() {
        int codigo = 0;
        try {
            codigo = miArchivo.ultimoCodigo() + 1;
        } catch (IOException ex) {
            Logger.getLogger(TicketServicio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return codigo;
    }

    @Override
    public int numRows() {
        int cantidad = 0;
        try {
            cantidad = miArchivo.cantidadFilas();
        } catch (IOException ex) {
            Logger.getLogger(TicketServicio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cantidad;
    }

    @Override
    public Boolean deleteFrom(int indice) {
        Boolean correcto = false;
        try {
            List<String> arregloDeDatos = miArchivo.borrarFilaPosicion(indice);
            if (!arregloDeDatos.isEmpty()) {
                correcto = true;
            }
        } catch (IOException ex) {
            Logger.getLogger(TicketServicio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return correcto;
    }

    @Override


public Boolean updateSet(int indice, TicketDto obj, String ruta) {
    boolean correcto = false;

    try {

        StringBuilder cadena = new StringBuilder();

        cadena.append(obj.getIdTicket())
              .append(Persistencia.SEPARADOR_COLUMNAS)
              .append(obj.getTipoEntradaTicket())
              .append(Persistencia.SEPARADOR_COLUMNAS)
              .append(obj.getIdClienteTicket() != null ? obj.getIdClienteTicket().getIdCliente() : 0)
              .append(Persistencia.SEPARADOR_COLUMNAS)
              .append(obj.getEsValidoTicket())
              .append(Persistencia.SEPARADOR_COLUMNAS)
              .append(obj.getFechaEmisionTicket())
              .append(Persistencia.SEPARADOR_COLUMNAS)

              // 🔥 CORRECCIÓN: precio sin exponenciales
              .append(new java.math.BigDecimal(
                      String.valueOf(obj.getPrecioTicket())
                     ).toPlainString()
              )
              .append(Persistencia.SEPARADOR_COLUMNAS)

              .append(obj.getNombreImagenPublicoTicket())
              .append(Persistencia.SEPARADOR_COLUMNAS);

        String nuevoPrivado;

        // ¿Cambió la imagen?
        if (ruta == null || ruta.isBlank()) {
            // NO se cambió: usar la antigua
            nuevoPrivado = obj.getNombreImagenPrivadoTicket();
        } else {

            // Sí cambió: guardar nueva
            nuevoPrivado = GestorImagen.grabarLaImagen(ruta);

            // Borrar antigua
            List<String> old = miArchivo.obtenerFila(indice);

            if (old != null && old.size() >= 8) {
                String oldImage = old.get(7).trim();
                if (!oldImage.isEmpty()) {
                    Path borrar = Paths.get(
                        Persistencia.RUTA_IMAGENES_EXTERNAS +
                        Persistencia.SEPARADOR_CARPETAS +
                        oldImage
                    );

                    try {
                        Files.deleteIfExists(borrar);
                    } catch (IOException e) {
                        System.out.println("No se pudo borrar la imagen antigua: " + oldImage);
                    }
                }
            }
        }

        cadena.append(nuevoPrivado);

        if (miArchivo.actualizaFilaPosicion(indice, cadena.toString())) {
            correcto = true;
        }

    } catch (Exception ex) {
        Logger.getLogger(TicketServicio.class.getName()).log(Level.SEVERE, null, ex);
    }

    return correcto;
}


    @Override
    public TicketDto getOne(int indice) {
        TicketDto objListo = null;
        try {
            List<String> arrDatos = miArchivo.obtenerFila(indice);
            if (arrDatos != null && arrDatos.size() >= 8) {

                int idTicket = Integer.parseInt(arrDatos.get(0).trim());
                String tipoEntrada = arrDatos.get(1).trim();
                int idCliente = Integer.parseInt(arrDatos.get(2).trim());
                Boolean esValido = Boolean.valueOf(arrDatos.get(3).trim());
                LocalDate fechaEmision = LocalDate.parse(arrDatos.get(4).trim());
                Double precio = Double.parseDouble(arrDatos.get(5).trim());
                String imagenPublica = arrDatos.get(6).trim();
                String imagenPrivada = arrDatos.get(7).trim();

                // Buscar cliente
                ClienteDto clienteDto = null;
                for (ClienteDto obj : ClienteControladorListar.obtenerTodos()) {
                    if (obj.getIdCliente() == idCliente) {
                        clienteDto = obj;
                        break;
                    }
                }

                if (clienteDto != null) {
                    objListo = new TicketDto(
                            idTicket,
                            tipoEntrada,
                            clienteDto,
                            esValido,
                            fechaEmision,
                            precio,
                            imagenPublica,
                            imagenPrivada
                    );
                }
            }
        } catch (IOException | NumberFormatException ex) {
            Logger.getLogger(TicketServicio.class.getName()).log(Level.SEVERE, null, ex);
        }
        return objListo;
    }
}
