#' @import sparklyr
#' @export
sparklyudf_register <- function(sc) {
  sparklyr::invoke_static(sc, "sparklyudf.Main", "register_hello", spark_session(sc))
}
