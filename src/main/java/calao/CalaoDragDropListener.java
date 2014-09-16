/***********************************************
This file is part of the Calao project (https://github.com/Neonunux/calao/wiki).

Calao is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Calao is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Calao.  If not, see <http://www.gnu.org/licenses/>.

**********************************************/
package calao;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class CalaoDragDropListener implements DropTargetListener {
	
	public void drop(DropTargetDropEvent event) {
		final Logger logger = LogManager
				.getLogger(CalaoDragDropListener.class.getName());
		// Accept copy drops
		event.acceptDrop(DnDConstants.ACTION_COPY);

		// Get the transfer which can provide the dropped item data
		Transferable transferable = event.getTransferable();

		try {
			List<File> dropppedFiles = (List<File>)transferable
					.getTransferData(DataFlavor.javaFileListFlavor);
		} catch (UnsupportedFlavorException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// Get the data formats of the dropped item
		DataFlavor[] flavors = transferable.getTransferDataFlavors();

		// Loop through the flavors
		for (DataFlavor flavor : flavors) {
			try {
				// If the drop items are files
				if (flavor.isFlavorJavaFileListType()) {
					// Get all of the dropped files
					List<File> files = (List<File>) transferable
							.getTransferData(flavor);
					// Loop them through
					for (File file : files) {
						Path source = Paths.get(file.getPath());
						String result;
						result = Files.probeContentType(source);
						if (result == null) {
							result = MimetypesFileTypeMap
									.getDefaultFileTypeMap().getContentType(
											file);
						}
						// Print out the file path
						logger.debug("result " + result);
						logger.debug("File path is **" + file.getPath() + "**");
					}
				}
			} catch (Exception e) {
				// Print out the error stack
				e.printStackTrace();
				logger.error(e);
			}
		}
		// Inform that the drop is complete
		event.dropComplete(true);
	}

	public void dragEnter(DropTargetDragEvent dtde) {
		return;
	}

	public void dragOver(DropTargetDragEvent dtde) {
		return;
	}

	public void dropActionChanged(DropTargetDragEvent dtde) {
		return;
	}

	public void dragExit(DropTargetEvent dte) {
		return;
	}

}