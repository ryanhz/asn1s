////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010-2017. Lapinin "lastrix" Sergey.                          /
//                                                                             /
// Permission is hereby granted, free of charge, to any person                 /
// obtaining a copy of this software and associated documentation              /
// files (the "Software"), to deal in the Software without                     /
// restriction, including without limitation the rights to use,                /
// copy, modify, merge, publish, distribute, sublicense, and/or                /
// sell copies of the Software, and to permit persons to whom the              /
// Software is furnished to do so, subject to the following                    /
// conditions:                                                                 /
//                                                                             /
// The above copyright notice and this permission notice shall be              /
// included in all copies or substantial portions of the Software.             /
//                                                                             /
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,             /
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES             /
// OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND                    /
// NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT                /
// HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,                /
// WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING                /
// FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE                  /
// OR OTHER DEALINGS IN THE SOFTWARE.                                          /
////////////////////////////////////////////////////////////////////////////////

package org.asn1s.api.type.x681;

import org.asn1s.api.Ref;
import org.asn1s.api.Scope;
import org.asn1s.api.exception.ResolutionException;
import org.asn1s.api.exception.ValidationException;
import org.asn1s.api.type.NamedType;
import org.asn1s.api.value.Value;
import org.jetbrains.annotations.NotNull;

public interface ClassFieldType<T extends Ref<T>> extends NamedType
{
	@NotNull
	ClassType getParent();

	void setParent( @NotNull ClassType parent );

	boolean hasDefault();

	Ref<T> getDefault();

	boolean isUnique();

	boolean isOptional();

	default boolean isRequired()
	{
		return !isOptional() && !hasDefault();
	}

	@Override
	default void accept( @NotNull Scope scope, @NotNull Ref<Value> valueRef ) throws ValidationException, ResolutionException
	{
		//noinspection unchecked
		acceptRef( scope, (Ref<T>)valueRef );
	}

	void acceptRef( @NotNull Scope scope, Ref<T> ref ) throws ResolutionException, ValidationException;

	T optimizeRef( @NotNull Scope scope, Ref<T> ref ) throws ResolutionException, ValidationException;

	Kind getClassFieldKind();

	enum Kind
	{
		TYPE,
		VALUE,
		VALUE_SET
	}
}
