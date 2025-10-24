
using ProyectoWPF.Models;
using ProyectoWPF.Services;
using GalaSoft.MvvmLight;
using System.Collections.ObjectModel;
using System.Windows.Input;
using ProyectoWPF.Data;
using GalaSoft.MvvmLight.Command;
using ProyectoWPF.ViewModels;


namespace ProyectoWPF.ViewModels {
    public class MainViewModel : ViewModelBase
    {
        private CarnesService _servicio = new CarnesService();

        // Colección observable para mostrar Carnes en el DataGrid
        public ObservableCollection<Carnes> Carnes { get; set; }

        public ICommand GuardarCommand { get; set; }

        public MainViewModel()
        {
            Carnes = new ObservableCollection<Carnes>();

            CargarCarnes();
        }

        private void CargarCarnes()
        {
            // Llama a la lógica de datos (que usa LINQ)
            var lista = _servicio.ObtenerCarnes();

            Carnes.Clear();
            foreach (var c in lista)
            {
                Carnes.Add(c);
            }
        }
    }
}