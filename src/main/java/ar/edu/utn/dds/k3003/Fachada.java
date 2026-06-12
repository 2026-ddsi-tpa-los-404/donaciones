package ar.edu.utn.dds.k3003;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.EstadoDonacionEnum;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.IdentificadorDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.ProductoDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.TipoIdentificadorEnum;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.QuejaDTO;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonaciones;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonadoresYEntidades;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaLogistica;
import ar.edu.utn.dds.k3003.exceptions.DonacionNoEncontradaException;
import ar.edu.utn.dds.k3003.exceptions.DonadorNoPuedeDonarException;
import ar.edu.utn.dds.k3003.model.Donacion;
import ar.edu.utn.dds.k3003.model.EstadoDonacion;
import ar.edu.utn.dds.k3003.model.Identificador;
import ar.edu.utn.dds.k3003.model.Producto;
import ar.edu.utn.dds.k3003.repositories.DataMapper.DonacionesDataMapper;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.CategoriaDTO;
import ar.edu.utn.dds.k3003.model.Categoria;
import java.util.UUID;
import ar.edu.utn.dds.k3003.repositories.DataMapper.CategoriasDataMapper;
import ar.edu.utn.dds.k3003.repositories.DataMapper.IdentificadoresDataMapper;
import ar.edu.utn.dds.k3003.repositories.DataMapper.ProductosDataMapper;
import ar.edu.utn.dds.k3003.repositories.InMemory.InMemoryCategoriaRepository;
import ar.edu.utn.dds.k3003.repositories.InMemory.InMemoryDonacionesRepository;
import ar.edu.utn.dds.k3003.repositories.InMemory.InMemoryIdentificadorRepository;
import ar.edu.utn.dds.k3003.repositories.InMemory.InMemoryProductoRepository;
import ar.edu.utn.dds.k3003.repositories.Repository.CategoriaRepository;
import ar.edu.utn.dds.k3003.repositories.Repository.DonacionesRepository;
import ar.edu.utn.dds.k3003.repositories.Repository.IdentificadorRepository;
import ar.edu.utn.dds.k3003.repositories.Repository.ProductoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class Fachada implements FachadaDonaciones {

  private final DonacionesRepository donacionesRepository;
  private final DonacionesDataMapper donacionesDataMapper;
  private final ProductoRepository productoRepository;
  private final ProductosDataMapper productosDataMapper;
  private final IdentificadorRepository identificadorRepository;
  private final IdentificadoresDataMapper identificadoresDataMapper;
  private final CategoriaRepository categoriaRepository;
  private final CategoriasDataMapper categoriasDataMapper;
  private FachadaDonadoresYEntidades fachadaDonadoresYEntidades;
  private FachadaLogistica fachadaLogistica;

  public Fachada() {
    this.donacionesRepository = new InMemoryDonacionesRepository();
    this.donacionesDataMapper = new DonacionesDataMapper();
    this.productoRepository = new InMemoryProductoRepository();
    this.productosDataMapper = new ProductosDataMapper();
    this.identificadorRepository = new InMemoryIdentificadorRepository();
    this.identificadoresDataMapper = new IdentificadoresDataMapper();
    this.categoriaRepository = new InMemoryCategoriaRepository();
    this.categoriasDataMapper = new CategoriasDataMapper();
  }

  public Fachada(
      DonacionesRepository donacionesRepository,
      ProductoRepository productoRepository,
      IdentificadorRepository identificadorRepository,
      CategoriaRepository categoriaRepository) {
    this.donacionesRepository = donacionesRepository;
    this.donacionesDataMapper = new DonacionesDataMapper();
    this.productoRepository = productoRepository;
    this.productosDataMapper = new ProductosDataMapper();
    this.identificadorRepository = identificadorRepository;
    this.identificadoresDataMapper = new IdentificadoresDataMapper();
    this.categoriaRepository = categoriaRepository;
    this.categoriasDataMapper = new CategoriasDataMapper();
  }

  @Override
  public DonacionDTO registrarDonacion(DonacionDTO donacionDTO) {
    if (donacionDTO == null){
      throw new IllegalArgumentException("INGRESO INVALIDO,");
    }
    if (donacionDTO.id() != null){
      throw new IllegalArgumentException("La donacion ya fue generada.");
    }
    this.fachadaDonadoresYEntidades.buscarDonadorPorID(donacionDTO.donadorID());

    Boolean puedeDonar = this.fachadaDonadoresYEntidades.puedeDonar(donacionDTO.donadorID());

    if (!puedeDonar) {
      throw new DonadorNoPuedeDonarException("El Donador no esta habilitado para esta operacion.");
    }

    String donacionID = UUID.randomUUID().toString();
    this.fachadaLogistica.gestionarDonacion(donacionDTO.depositoID(), donacionID, donacionDTO.productoID(), donacionDTO.cantidad());

    Donacion donacion = this.donacionesDataMapper.toDonacion(donacionDTO);
    donacion.setId(donacionID);
    this.cambiarEstado(donacion , EstadoDonacionEnum.INGRESADA);
    donacion.setFecha(LocalDateTime.now());

    donacion = this.donacionesRepository.save(donacion);
    return this.donacionesDataMapper.toDonacionDTO(donacion);
  }

  @Override
  public DonacionDTO buscarDonacionPorID(String donacionID) throws DonacionNoEncontradaException {
    return donacionesRepository
        .findById(donacionID)
        .map(donacionesDataMapper::toDonacionDTO)
        .orElseThrow(() -> new DonacionNoEncontradaException("No existe la donacion"));
  }

  @Override
  public DonacionDTO cambiarEstadoDeDonacion(String donacionID, EstadoDonacionEnum estado) throws DonacionNoEncontradaException {
    if ( estado== null){
      throw new IllegalArgumentException("El Estado esta vacio: Invalido");
    }
    Donacion donacion =
            this.donacionesRepository.findById(donacionID).orElseThrow(() -> new DonacionNoEncontradaException("No se encuentra la Donacion."));

    if (estado == EstadoDonacionEnum.ACEPTADA && donacion.getEstado() != EstadoDonacionEnum.INGRESADA) {
      throw new IllegalArgumentException("Para cambiar a ACEPTADA el estado previo debe ser INGRESADA.");
    }
    if (estado == EstadoDonacionEnum.CONQUEJA && donacion.getEstado() != EstadoDonacionEnum.ACEPTADA) {
      throw new IllegalArgumentException("Para cambiar a CONQUEJA el estado previo debe ser ACEPTADA.");
    }

    this.cambiarEstado(donacion, estado);
    donacion = this.donacionesRepository.save(donacion);
    return this.donacionesDataMapper.toDonacionDTO(donacion);
  }

  @Override
  public List<DonacionDTO> buscarPorDonadorYFechaInicio(String donadorID, LocalDate fecha) throws DonacionNoEncontradaException {
    List<Donacion> donaciones = this.donacionesRepository.findByDonadorID(donadorID);

    if (donaciones.isEmpty()){
      throw new DonacionNoEncontradaException("No existe una donacion con ese ID y Fecha.");
    }

    return donaciones.stream()
        .filter(don -> !don.getFecha().toLocalDate().isBefore(fecha))
        .map(donacionesDataMapper::toDonacionDTO)
        .collect(Collectors.toList());
  }

  @Override
  public DonacionDTO registrarQuejaEnDonacion(String donacionID, String descripcion) {
    if (donacionID == null ){
      throw new IllegalArgumentException("Ninguna donacion Ingresada: Invalido");
    }

    Donacion donacion = this.donacionesRepository.findById(donacionID).orElseThrow(() -> new DonacionNoEncontradaException("Donacion no Encontrada."));

    this.fachadaDonadoresYEntidades.agregarQueja(new QuejaDTO(null, donacionID, donacion.getDonadorID(), null , descripcion ));

    this.cambiarEstado(donacion, EstadoDonacionEnum.CONQUEJA);
    donacion = this.donacionesRepository.save(donacion);
    return this.donacionesDataMapper.toDonacionDTO(donacion);
  }

  @Override
  public ProductoDTO agregarProducto(ProductoDTO productoDTO) {
    if (productoDTO.identificadorID() != null) {
      Identificador identificador =
          this.identificadorRepository
              .findById(productoDTO.identificadorID())
              .orElseThrow(
                  () -> new NoSuchElementException("Identificador Invalido: " + productoDTO.identificadorID()));

      if (identificador.getTipo() == TipoIdentificadorEnum.CODIGODEBARRAS) {
        String[] palabras = productoDTO.descripcion().trim().split("\\s+");
        if (palabras.length < 3) {
          throw new IllegalArgumentException("La descripcion debe tener al menos 3 palabras para CODIGODEBARRAS");
        }
      } else if (identificador.getTipo() == TipoIdentificadorEnum.QR) {
        long cantLetras = productoDTO.nombre().chars().filter(Character::isLetter).count();
        if (cantLetras % 2 != 0) {
          throw new IllegalArgumentException("El nombre debe tener cantidad par de letras para QR");
        }
      }
    }

    Producto producto = productosDataMapper.toProducto(productoDTO);
    this.productoRepository.save(producto);
    return productosDataMapper.toProductoDTO(producto);
  }

  @Override
  public ProductoDTO buscarProductoPorID(String productoID) throws NoSuchElementException {
    Producto producto =
        this.productoRepository.findById(productoID)
            .orElseThrow(() -> new NoSuchElementException("Producto no encontrado: " + productoID));
    return productosDataMapper.toProductoDTO(producto);
  }

  public List<ProductoDTO> listarProductos() {
    return productoRepository.findAll().stream()
        .map(productosDataMapper::toProductoDTO)
        .collect(Collectors.toList());
  }

  public ProductoDTO actualizarProducto(String id, ProductoDTO productoDTO) {
    Producto producto =
        this.productoRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Producto no encontrado: " + id));
    producto.setNombre(productoDTO.nombre());
    producto.setDescripcion(productoDTO.descripcion());
    producto.setSubCategoriaID(productoDTO.categoriaID());
    producto.setIdentificadorID(productoDTO.identificadorID());
    producto = this.productoRepository.save(producto);
    return productosDataMapper.toProductoDTO(producto);
  }

  public ProductoDTO eliminarProductoPorID(String id) {
    Producto producto =
        this.productoRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Producto no encontrado: " + id));
    ProductoDTO dto = productosDataMapper.toProductoDTO(producto);
    this.productoRepository.deleteById(id);
    return dto;
  }

  @Override
  public IdentificadorDTO agregarIdentificador(IdentificadorDTO identificadorDTO) {
    Identificador identificador = identificadoresDataMapper.toIdentificador(identificadorDTO);
    this.identificadorRepository.save(identificador);
    return identificadoresDataMapper.toIdentificadorDTO(identificador);
  }

  @Override
  public IdentificadorDTO buscarIdentificadorPorID(String identificadorID) throws NoSuchElementException {
    Identificador identificador =
        this.identificadorRepository.findById(identificadorID)
            .orElseThrow(() -> new NoSuchElementException("Identificador no encontrado: " + identificadorID));
    return identificadoresDataMapper.toIdentificadorDTO(identificador);
  }

  public List<IdentificadorDTO> listarIdentificadores() {
    return identificadorRepository.findAll().stream()
        .map(identificadoresDataMapper::toIdentificadorDTO)
        .collect(Collectors.toList());
  }

  public IdentificadorDTO eliminarIdentificadorPorID(String id) {
    Identificador identificador =
        this.identificadorRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Identificador no encontrado: " + id));
    IdentificadorDTO dto = identificadoresDataMapper.toIdentificadorDTO(identificador);
    this.identificadorRepository.deleteById(id);
    return dto;
  }

  public CategoriaDTO agregarCategoria(CategoriaDTO categoriaDTO) {
    Categoria categoria = categoriasDataMapper.toCategoria(categoriaDTO);
    this.categoriaRepository.save(categoria);
    return categoriasDataMapper.toCategoriaDTO(categoria);
  }

  public CategoriaDTO buscarCategoriaPorID(String id) {
    Categoria categoria =
        this.categoriaRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Categoria no encontrada: " + id));
    return categoriasDataMapper.toCategoriaDTO(categoria);
  }

  public List<CategoriaDTO> listarCategorias() {
    return categoriaRepository.findAll().stream()
        .map(categoriasDataMapper::toCategoriaDTO)
        .collect(Collectors.toList());
  }

  public CategoriaDTO eliminarCategoriaPorID(String id) {
    Categoria categoria =
        this.categoriaRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Categoria no encontrada: " + id));
    CategoriaDTO dto = categoriasDataMapper.toCategoriaDTO(categoria);
    this.categoriaRepository.deleteById(id);
    return dto;
  }

  public List<DonacionDTO> listarDonaciones() {
    return donacionesRepository.findAll().stream().map(donacion -> donacionesDataMapper.toDonacionDTO(donacion)).collect(Collectors.toList());
  }

  public DonacionDTO eliminarDonacionPorID(String id) {
    Donacion donacion =
        this.donacionesRepository.findById(id).orElseThrow(() -> new DonacionNoEncontradaException("No existe la donacion con id: " + id));
    DonacionDTO dto = this.donacionesDataMapper.toDonacionDTO(donacion);
    this.donacionesRepository.deleteById(id);
    return dto;
  }

  @Override
  public void setFachadaDonadoresYEntidades(FachadaDonadoresYEntidades fachadaDonadoresYEntidades) {
    this.fachadaDonadoresYEntidades = fachadaDonadoresYEntidades;
  }

  @Override
  public void setFachadaLogistica(FachadaLogistica fachadaLogistica) {
    this.fachadaLogistica = fachadaLogistica;
  }

  private void cambiarEstado(Donacion donacion, EstadoDonacionEnum nuevoEstado) {
    donacion.setEstado(nuevoEstado);

    EstadoDonacion registro = new EstadoDonacion();
    registro.setEstado(nuevoEstado);
    registro.setTiempo(LocalDateTime.now());
    registro.setDonacion(donacion);

    donacion.getHistorialEstados().add(registro);
  }
}
