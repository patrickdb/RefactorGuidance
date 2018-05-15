/**
 Copyright (C) 2018, Patrick de Beer

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **/
package analysis.context;

import analysis.MethodAnalyzer.ClassMethodFinder;

public class MethodOverload implements IContextDetector {

    private ClassMethodFinder _analyzer = null;
    private String _methodName = null;

    public MethodOverload(ClassMethodFinder cmf, String methodName) {
        this._analyzer = cmf;
        this._methodName = methodName;
    }

    @Override
    public boolean detect() throws Exception {
        return false;
    }
}
