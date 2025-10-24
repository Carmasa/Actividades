using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;
using ProyectoWPF.ViewModels;

namespace ProyectoWPF
{
    /// <summary>
    /// Lógica de interacción para Window1.xaml
    /// </summary>
    public partial class Productos : MainViewModel
    {
        private readonly MainViewModel _vm;

        public Productos()
        {
            InitializeComponent();

            // Usamos el MainViewModel que ya contiene la colección Carnes y carga los datos
            _vm = new MainViewModel();
            this.DataContext = _vm;
        }

        private void BtnCerrar_Click(object sender, RoutedEventArgs e)
        {
            var principalWindow = new MainWindow();
            principalWindow.Show();
            this.Close();
        }

        private void DataGrid_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {

        }
    }
}
